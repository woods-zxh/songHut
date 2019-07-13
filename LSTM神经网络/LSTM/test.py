#Author 张新豪
#test.py用于测试模型
import time
from collections import namedtuple
import os
import numpy as np
import tensorflow as tf
import json
from model import get_neural_chord
import re

str = "abcdeefG"
samp = get_neural_chord(len(str), prime=str)
print(samp)
print("hello")
