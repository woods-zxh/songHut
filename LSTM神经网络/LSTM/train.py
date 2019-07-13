#Author 张新豪
#tain.py用于模型的训练
import time
from collections import namedtuple
import os
import numpy as np
import tensorflow as tf
import re
from LSTM import *
from data_dealwith import get_sum_data
#1 数据加载与预处理
vocab = [1,2,3,4,5,6,7,8,9,10,11,12,0]
vocab_to_int = {"r":0,"c":1,"C":2,"d":3,"D":4,"e":5,"f":6,"F":7,"g":8,"G":9,"a":10,"A":11,"b":12}
int_to_vocab = {0:"r",1:"c",2:"C",3:"d",4:"D",5:"e",6:"f",7:"F",8:"g",9:"G",10:"a",11:"A",12:"b"}
path_melody = "../pitch/data/melody/"  # 待读取的文件夹
path_chord ="../pitch/data/chord/"  #待读取的文件夹
melody_chord = get_sum_data(path_melody,path_chord)
print(melody_chord[0])
print(melody_chord[1])
encoded_note = np.array([vocab_to_int[c] for c in melody_chord[0]], dtype=np.int32)
encoded_chord = np.array([vocab_to_int[c] for c in melody_chord[1]], dtype=np.int32)

batch_size = 2        # Sequences per batch
num_steps =  8        # Number of sequence steps per batch，这里其实也可以自己定
lstm_size = 256  # Size of hidden layers in LSTM 这个可以自己定
num_layers = 2        # Number of LSTM layers  这个也可以自己定
learning_rate = 0.001    # Learning rate
keep_prob = 0.5         # Dropout keep probability
#迭代的次数
epochs = 60
# 每n轮进行一次变量保存
save_every_n = 10000

model = LSTM(len(vocab), batch_size=batch_size, num_steps=num_steps,
                lstm_size=lstm_size, num_layers=num_layers,
                learning_rate=learning_rate)

saver = tf.train.Saver(max_to_keep=100)
with tf.Session() as sess:
    sess.run(tf.global_variables_initializer())
    counter = 0
    for e in range(epochs):
        # Train network
        new_state = sess.run(model.initial_state)
        loss = 0
        for x, y in get_batches(encoded_note,encoded_chord, batch_size, num_steps):
            counter += 1
            start = time.time()
            feed = {model.inputs: x,
                    model.targets: y,
                    model.keep_prob: keep_prob,
                    model.initial_state: new_state}
            batch_loss, new_state, _ = sess.run([model.loss,
                                                 model.final_state,
                                                 model.optimizer],
                                                 feed_dict=feed)

            end = time.time()
            # control the print lines
            if counter % 500 == 0:
                print('轮数: {}/{}... '.format(e+1, epochs),
                      '训练步数: {}... '.format(counter),
                      '训练误差: {:.4f}... '.format(batch_loss),
                      '{:.4f} sec/batch'.format((end-start)))
            #
            # if (counter % save_every_n == 0):
            #     saver.save(sess, "checkpoints/i{}_l{}.ckpt".format(counter, lstm_size))
        print(time.localtime())
        saver.save(sess, "checkpoints/i{}_l{}.ckpt".format(counter, lstm_size))

