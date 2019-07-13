#Author 张新豪
#用于搭建网络
import time
from collections import namedtuple
import os
import numpy as np
import tensorflow as tf
import re

# 构造生成器，对已有的数组进行mini-batch分割
def get_batches(arr_in, arr_out ,n_seqs, n_steps):
    '''
    arr: 待分割的数组
    n_seqs: 一个batch中序列个数
    n_steps: 单个序列包含的字符数
    '''
    batch_size = n_seqs * n_steps
    n_batches = int(len(arr_in) / batch_size)
    # 这里我们仅保留完整的batch，对于不能整出的部分进行舍弃
    arr_in = arr_in[:batch_size * n_batches]

    batch_size = n_seqs * 1
    n_batches = int(len(arr_out) / batch_size)
    # 这里我们仅保留完整的batch，对于不能整出的部分进行舍弃
    arr_out = arr_out[:batch_size * n_batches]
    # 重塑
    arr_in = arr_in.reshape((n_seqs, -1))
    arr_out = arr_out.reshape((n_seqs, -1))
    for n in range(0, arr_in.shape[1], n_steps):
        # inputs
        x = arr_in[:, n:n + n_steps]
        # targets

        y = arr_out[: ,n: n+ n_steps]

        yield x ,y


#模型构建
# 模型构建部分主要包括了输入层，LSTM层，输出层，loss，optimizer等部分的构建，我们将一块一块来进行实现。

# 输入层
#  构建输入层
def build_inputs(num_seqs, num_steps):
    '''
    num_seqs: 每个batch中的序列个数
    num_steps: 每个序列包含的字符数
    '''
    inputs = tf.placeholder(tf.int32, shape=(num_seqs, num_steps), name='inputs')
    targets = tf.placeholder(tf.int32, shape=(num_seqs, num_steps), name='targets')

    # with tf.variable_scope('encoder'):
    #     encoder_w = tf.Variable(tf.truncated_normal([num_steps, out_size], stddev=0.1))
    #     encoder_b = tf.Variable(tf.zeros(out_size))
    #
    #     # 计算encoder
    #     encoder_w = tf.cast(encoder_w,tf.int32)
    #     encoder_b =tf.cast(encoder_b,tf.int32)
    # encoder = tf.matmul(inputs, encoder_w) + encoder_b
    # 加入keep_prob
    keep_prob = tf.placeholder(tf.float32, name='keep_prob')

    return inputs,targets, keep_prob


#  构建lstm层
def build_lstm(lstm_size, num_layers, batch_size, keep_prob):
    '''
    keep_prob
    lstm_size: lstm隐层中结点数目
    num_layers: lstm的隐层数目
    batch_size: batch_size
    '''
    # # 构建一个基本lstm单元
    # lstm = tf.contrib.rnn.BasicLSTMCell(lstm_size)
    def LstmCell():
        lstm = tf.contrib.rnn.BasicLSTMCell(lstm_size)
        return tf.contrib.rnn.DropoutWrapper(lstm, output_keep_prob=keep_prob)
    # 添加dropout,用于正则化
    # drop = tf.contrib.rnn.DropoutWrapper(lstm, output_keep_prob=keep_prob)
    #
    # # 堆叠出num_layers层的神经网络
    # cell =tf.contrib.rnn.MultiRNNCell([drop for _ in range(num_layers)])
    cell = tf.contrib.rnn.MultiRNNCell([LstmCell() for _ in range(num_layers)])
    initial_state = cell.zero_state(batch_size, tf.float32)

    return cell, initial_state

# 构造输出层
def build_output(lstm_output, in_size, out_size):
    '''
    lstm_output: lstm层的输出结果
    in_size: lstm输出层重塑后的size
    out_size: softmax层的size
    '''
    seq_output = tf.concat(lstm_output, axis=1  )# tf.concat(concat_dim, values)
    # reshape
    x = tf.reshape(seq_output, [-1, in_size])
    # 将lstm层与softmax层全连接
    with tf.variable_scope('softmax'):
        softmax_w = tf.Variable(tf.truncated_normal([in_size, out_size], stddev=0.1))
        softmax_b = tf.Variable(tf.zeros(out_size))

    # 计算logits
    logits = tf.matmul(x, softmax_w) + softmax_b

    # softmax层返回概率分布
    out = tf.nn.softmax(logits, name='predictions')

    return out, logits

#  根据logits和targets计算损失
def build_loss(logits, targets, lstm_size, num_classes):
    '''
    logits: 全连接层的输出结果（不经过softmax）
    targets: targets
    lstm_size
    num_classes: vocab_size
    '''

    # One-hot编码
    y_one_hot = tf.one_hot(targets, num_classes)
    y_reshaped = tf.reshape(y_one_hot, logits.get_shape())

    # Softmax cross entropy loss
    loss = tf.nn.softmax_cross_entropy_with_logits(logits=logits, labels=y_reshaped)
    loss = tf.reduce_mean(loss)

    return loss

def build_optimizer(loss, learning_rate, grad_clip):
    tvars = tf.trainable_variables()
    grads, _ = tf.clip_by_global_norm(tf.gradients(loss, tvars), grad_clip)
    train_op = tf.train.AdamOptimizer(learning_rate)
    optimizer = train_op.apply_gradients(zip(grads, tvars))

    return optimizer

class LSTM:
    def __init__(self, num_classes, batch_size=64, num_steps=50,
                       lstm_size=128, num_layers=2, learning_rate=0.001,
                       grad_clip=5, sampling=False):

        # 如果sampling是True，则采用SGD，梯度下降
        if sampling == True:
            batch_size, num_steps = 1, 1
        else:
            batch_size, num_steps = batch_size, num_steps

        tf.reset_default_graph()
        # 输入层
        self.inputs, self.targets, self.keep_prob = build_inputs(batch_size, num_steps)
        # LSTM层
        cell, self.initial_state = build_lstm(lstm_size, num_layers, batch_size, self.keep_prob)
        # 对输入进行one-hot编码
        x_one_hot = tf.one_hot(self.inputs, num_classes)
        # 运行RNN
        outputs, state = tf.nn.dynamic_rnn(cell, x_one_hot, initial_state=self.initial_state)
        self.final_state = state

        # 预测结果
        self.prediction, self.logits = build_output(outputs, lstm_size, num_classes)

        # Loss 和 optimizer (with gradient clipping)
        self.loss = build_loss(self.logits, self.targets, lstm_size, num_classes)
        self.optimizer = build_optimizer(self.loss, learning_rate, grad_clip)
