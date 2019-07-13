#Author 张新豪
import librosa
#import librosa.display
import librosa.util
import numpy as np
import pandas as pd
import mido
import os
from .util import *
from .accompany import *
# from mid2audio44 import midi2wav
# import pysynth
# import pygame   # midi playback
# from play_music import play_music

#筛选的毫秒数
notes = ["c4","c#4","d4","d#4","e4","f4","f#4","g4","g#4","a4","a#4","b4","r"]
notes_to_input = {"c4":"c","c#4":"C","d4":"d","d#4":"D","e4":"e","f4":"f","f#4":"F","g4":"g","g#4":"G","a4":"a","a#4":"A","b4":"b","r":"r"}
result = []
div =7 #越大就越识别越精细
len1 = 22
slow = 0.9#计算过程中的损失
is_lstm =True
pd.set_option('display.width', 1000)  # 设置字符显示宽度
pd.set_option('display.max_rows', None)  # 设置显示最大行
pd.set_option('display.max_columns', None)  # 设置显示最大行


#为使用pysynth预备，转换成（"c",4)序列，这里4是四分音符的意思
def transfer(notes_values,mean_beat):
    filter = round(mean_beat/4)
    result1 = []
    temp = 0
    count = 0
    for note in notes_values:
        if temp != note and count!=0:
            if(count<=filter):
                result1.append((notes[12], 320 / count))
            else:
                result1.append((notes[temp - 1], 320/count))
            temp = note
            count = 1
        else:
            count = count + 1
    return result1


#为Midi预备转换成("c",480)的序列，这里480是tick的意思，一个四分音符120个tick,拍速是一分钟内有多少拍
def transfer_frames(notes_values,mean_beat):
    filter = round(mean_beat/div)
    # print(filter)
    result1 = []
    temp = [0,-1]#保存前一个音符
    tempf = 0
    count = 0
    for i,frame in enumerate(notes_values):
        if temp[1] != frame and count!=0:
            if(count<filter ):
                if temp[0] != temp[1]:
                    result1.append((notes[12],  count))
                else:
                    result1.append((notes[temp[0]], count))
            else:
                result1.append((notes[temp[1] - 1], count))
            temp[0] = temp[1]
            temp[1] = frame
            count = 1
        else:
            count = count + 1
    result1.append((notes[temp[0]], count))
    return result1

#为两拍去配和弦
def clip_make(notes_values,mean_beat):
    result1 = []
    count = 0
    clap = 0
    clap_notes = [[]]
    mean_beat = 2*round(mean_beat)
    for frame in notes_values:
        if count % mean_beat == 0 and count != 0:
            clap = clap + 1
            clap_notes.append([])
        count = count + 1
        clap_notes[clap].append(frame)
    for clap in clap_notes:
        result1.append(transfer_frames(clap,mean_beat))
    return result1

#将不规则的用户旋律转换为
def transfer_note(clips,mean_beat):
    clips_notes_str = ""
    for clip in clips:
        # print(clip)
        note_str =""
        for frame in clip:
            time_reverse =(mean_beat/frame[1])
            time_reverse = round(time_reverse ,1)
            input_text = notes_to_input[frame[0]]
            if(len(note_str)>=8):
                break
            if(time_reverse>4.5):
                continue
            elif(time_reverse <= 4.5 and time_reverse >3.5):
                note_str = note_str +input_text
            elif(time_reverse <=3.5 and time_reverse>1.8):
                note_str = note_str + input_text*2
                if (len(note_str) >= 8):
                    note_str = note_str[:8]
                    break
            elif(time_reverse<=1.8 and time_reverse>1.2):
                note_str = note_str + input_text * 3
                if (len(note_str) >= 8):
                    note_str = note_str[:8]
                    break
            elif(time_reverse<=1.2 and time_reverse>0.6):
                note_str = note_str + input_text * 4
                if (len(note_str) >= 8):
                    note_str = note_str[:8]
                    break
        if (len(note_str) < 8):
            note_str = note_str + "r"*8
            note_str = note_str[:8]
        clips_notes_str = clips_notes_str +note_str
    return clips_notes_str

#根据音符序列制作Midi文件
def make_midi(input_wav,notes,tempo,mean_beat,instrument,velocity):
    mid = mido.MidiFile()
    track = mido.MidiTrack()
    mid.tracks.append(track)
    tempo = mido.bpm2tempo(tempo)
    track.append(mido.MetaMessage('set_tempo', tempo=round(tempo*slow), time=0))
    track.append(mido.Message('program_change', program=instrument, time=0))
    for note in notes:
        gap = int(round((note[1] * 480) / mean_beat))
        if(note[0]=='r'):
            track.append(mido.Message('note_on', note=60, velocity=0, time=0))
            track.append(mido.Message('note_off', note=60, velocity=0, time=gap))
        else:
            note_num = librosa.note_to_midi(note[0])
            track.append(mido.Message('note_on', note=note_num, velocity=velocity, time=0))
            track.append(mido.Message('note_off', note=note_num, velocity=velocity, time=gap))
    output_midi = getFilename(input_wav,instrument)

    # mid.save(output_midi)
    return  mid,output_midi

def humToMelody(input_wav,instrument,is_drum=0,is_bass=0,is_chord=0):
    pd.set_option('display.width', 1000)  # 设置字符显示宽度
    pd.set_option('display.max_rows', None)  # 设置显示最大行
    pd.set_option('display.max_columns', None)  # 设置显示最大行
    #y是时域被采样后的离散的波形图，x为时间,y为振幅
    #sr是采样率
    y,sr=librosa.load(input_wav,sr=None,duration=None)
    #获得伴奏的节拍数，以及时间间隔
    tempo, beats = librosa.beat.beat_track(y=y, sr=sr)
    beats = librosa.frames_to_time(beats,sr=sr)
    beat_gap = librosa.feature.delta(beats)
    #平均节拍间隔
    # 转化为毫秒
    beat_gap = beat_gap*100
    mean_beat = np.mean(beat_gap)
    #节拍数
    beat_num = np.count_nonzero(beat_gap)
    #chroma是色度的矩阵，为12行，568列
    chroma=librosa.feature.chroma_cqt(y=y, sr=sr)#x是时间
    #dataframe是一个加索引，做表的函数
    c=pd.DataFrame(chroma)
    #c0现在是布尔值
    c0=(c==1)
    c1=c0.astype(int)
    labels=np.array(range(1,13))
    #dot是矩阵乘法
    note_values=labels.dot(c1)
    # #使用pysynth
    # result = transfer(note_values)
    # print(result)
    # song = tuple(result)
    # output_wav = "./wav/result.wav"
    # pysynth.make_wav(song, bpm=67, repeat=0, fn=output_wav,pause = 0)

    #使用Midi,并传入选择的乐器，默认为钢琴
    # result = clip_make(note_values,mean_beat)
    note_nums = transfer_frames(note_values,mean_beat)
    output_midi =  make_midi(input_wav,note_nums,tempo,mean_beat,instrument,100)#旋律音
    chord_clip = clip_make(note_values, mean_beat)
    notes_clip_str = transfer_note(chord_clip,mean_beat)
    #配置伴奏
    if (is_drum):
        output_midi = add_drum(output_midi[0],output_midi[1], tempo, mean_beat, beat_num - 1, 112, 110)  # 加鼓
    if (is_bass):
        output_midi = add(output_midi[0],output_midi[1], tempo, mean_beat, note_nums, 32, 80, -24)  # 加bass
    if (is_chord):
        output_midi = addChord(output_midi[0],output_midi[1], chord_clip,notes_clip_str, tempo, 90, 0,lstm = is_lstm)
    output_midi = add(output_midi[0],output_midi[1],tempo,mean_beat, note_nums,32,80,-12)#加其他
    output_midi = add(output_midi[0],output_midi[1],tempo,mean_beat, note_nums,32,40,-4)#加其他
    output_midi = add(output_midi[0],output_midi[1],tempo,mean_beat, note_nums, 32, 40, +4)  #加其他
    output_midi[0].save(output_midi[1])
    # output_wav = midi2wav(output_midi[1])
    return output_midi[1]


if __name__ == '__main__':
    print("in __main__")
    input_wav = r".\wav\result.wav"
    #输入要转换的输入wav音频文件
    instrument = 40
    output_midi = humToMelody(input_wav,instrument,1,0,1)
    # ab_output_midi = os.path.abspath(output_midi)
    print(output_midi)
    # play_music(output_midi)