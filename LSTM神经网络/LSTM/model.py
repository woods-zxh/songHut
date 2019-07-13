#Author 张新豪
#model.py用于模型的使用
import numpy as np
import tensorflow as tf
import re
from .LSTM import LSTM
lstm_size = 256  # Size of hidden layers in LSTM 这个可以自己定
vocab = [1,2,3,4,5,6,7,8,9,10,11,12,0]
vocab_to_int = {"r":0,"c":1,"C":2,"d":3,"D":4,"e":5,"f":6,"F":7,"g":8,"G":9,"a":10,"A":11,"b":12}
int_to_vocab = {0:"r",1:"c",2:"C",3:"d",4:"D",5:"e",6:"f",7:"F",8:"g",9:"G",10:"a",11:"A",12:"b"}

#预测结果中选取前top_n个最可能的字符
#preds: 预测结果
def pick_top_n(preds, vocab_size, top_n=5):
    p = np.squeeze(preds)
    # 将除了top_n个预测值的位置都置为0
    p[np.argsort(p)[:-top_n]] = 0
    # 归一化概率
    p = p / np.sum(p)
    # 随机选取一个字符
    c = np.random.choice(vocab_size, 1, p=p)[0]
    return c

#获取字符串中出现最多字符
def checkio(text):
    return max(text, key=text.count)

#根据用户输入生成结果样本
def sample(n_samples, prime="abc", lstm_size = lstm_size, vocab_size = len(vocab)):
    """
    checkpoint: 某一轮迭代的参数文件
    n_sample: 新闻本的字符长度
    lstm_size: 隐层结点数
    vocab_size
    prime: 起始文本
    """
    checkpoint = tf.train.latest_checkpoint('checkpoints')
    samples = [c for c in prime]
    # sampling=True意味着batch的size=1 x 1
    model = LSTM(len(vocab), lstm_size=lstm_size, sampling=True)
    saver = tf.train.Saver()
    with tf.Session() as sess:
        # 加载模型参数
        saver.restore(sess, checkpoint)
        new_state = sess.run(model.initial_state)
        for c in prime:
            x = np.zeros((1, 1))
            # 输入单个字符
            x[0,0] = vocab_to_int[c]
            feed = {model.inputs: x,
                    model.keep_prob: 1.,
                    model.initial_state: new_state}
            preds, new_state = sess.run([model.prediction, model.final_state],
                                         feed_dict=feed)

        c = pick_top_n(preds, len(vocab))
        # 添加字符到samples中
        samples.append(int_to_vocab[c])

        # 不断生成字符，直到达到指定数目
        for i in range(n_samples):
            x[0,0] = c
            feed = {model.inputs: x,
                    model.keep_prob: 1.,
                    model.initial_state: new_state}
            preds, new_state = sess.run([model.prediction, model.final_state],
                                         feed_dict=feed)

            c = pick_top_n(preds, len(vocab))
            samples.append(int_to_vocab[c])


    return ''.join(samples)

#根据结果样本，进行转化，获得神经网络生产的数据
def get_neural_chord(n_samples, prime):
    sample1 = sample(n_samples,prime)
    clips = re.findall(r'.{8}', sample1)
    chords = []
    for clip in clips:
        # print(clip)
        result = checkio(clip)
        if(result=="r"):
            result = "c"
        chords.append(checkio(clip))
    return chords
