#Author 张新豪
import mido
import librosa
from .util import *
from anna_lstm.model import get_neural_chord
slow = 0.9#计算过程中的损失
notes = ["c4","c#4","d4","d#4","e4","f4","f#4","g4","g#4","a4","a#4","b4","r"]
output_to_note = {"c":"c4","C":"c#4","d":"d4","D":"d#4","e":"e4","f":"f4","F":"f#4","g":"g4","G":"g#4","a":"a4","A":"a#4","b":"b4","r":"c4"}

#加鼓
def add_drum(mid1,output_midi,tempo,mean_beat,beat_num,type,velocity):
    mid = mid1
    track2 = mido.MidiTrack()
    mid.tracks.append(track2)
    tempo = mido.bpm2tempo(tempo)
    track2.append(mido.MetaMessage('set_tempo', tempo=round(tempo*slow), time=0))
    gap = 480
    for i in range(0,beat_num):
        track2.append(mido.Message('note_on', note=60, velocity=0, time=0))
        track2.append(mido.Message('note_off', note=60, velocity=0, time=gap))
        if(i%2 ==1):
            track2.append(mido.Message('program_change', program=type, time=0))  # 这个音轨使用的乐器
            track2.append(mido.Message('note_on',note = 24, velocity=velocity -30, time=0))
            track2.append(mido.Message('note_off',note = 24, velocity=velocity -30, time=0))
        else:
            track2.append(mido.Message('program_change', program=type, time=0))  # 这个音轨使用的乐器
            track2.append(mido.Message('note_on', note=14, velocity=velocity, time=0))
            track2.append(mido.Message('note_off', note=14, velocity=velocity, time=0))

    output_midi = getFilename(output_midi,type)
    # mid.save(output_midi)
    return mid,output_midi

#添加混音，通过使用偏置实现
def add(mid1,output_midi,tempo,mean_beat,notes,instrument,velocity,offset=0):
    mid = mid1
    track = mido.MidiTrack()
    mid.tracks.append(track)
    tempo = mido.bpm2tempo(tempo)
    track.append(mido.MetaMessage('set_tempo', tempo=round(tempo*slow), time=0))
    for note in notes:
        track.append(mido.Message('program_change', program=instrument, time=0))
        gap = int(round((note[1] * 480) / mean_beat))
        if(note[0]=='r'):
            track.append(mido.Message('note_on', note=60, velocity=0, time=0))
            track.append(mido.Message('note_off', note=60, velocity=0, time=gap))
        else:
            note_num = librosa.note_to_midi(note[0])
            track.append(mido.Message('note_on', note=note_num+offset, velocity=velocity, time=0))
            track.append(mido.Message('note_off', note=note_num+offset, velocity=velocity, time=gap))
    output_midi = getFilename(output_midi,instrument)
    # mid.save(output_midi)
    return mid,output_midi

#获得和弦序列,使用的算法是最长音配和弦
def get_chord_list(chord_clip):
    note_distribution = {}
    roots = []
    for clip in chord_clip:
        note_count ={
            "c4":0,
            "c#4":0,
            "d4": 0,
            "d#4":0,
            "e4":0,
            "f4":0,
            "f#4":0,
            "g4":0,
            "g#4":0,
            "a4":0,
            "a#4":0,
            "b4":0,
        }
        #获得计数
        for note in clip:
            if(note[0]!='r'):
                note_count[note[0]] =  note_count[note[0]]+note[1]
        max_num = max(note_count.values())
        root = get_key(note_count,max_num)
        roots.append(root)
    return roots

#获得和弦序列，使用的方法是LSTM神经网络
def get_neural_chords(notes_clip_str):
    #送进神经网络，批量处理
    print("helo")
    chords = get_neural_chord(len(notes_clip_str),notes_clip_str)
    real_chords =[]
    for output in chords:
        real_chords.append(output_to_note[output])
    return real_chords

#为旋律增加和弦
def addChord(mid1,output_midi,chord_clip,notes_clip_str,tempo,velocity,style,lstm):
    mid = mid1
    track = mido.MidiTrack()
    mid.tracks.append(track)
    tempo = mido.bpm2tempo(tempo)
    track.append(mido.MetaMessage('track_name', name='Piano 1', time=0))
    track.append(mido.MetaMessage('set_tempo', tempo=round(tempo * slow), time=0))
    track.append(mido.Message('program_change', program=1, time=0))  # 这个音轨使用的乐器
    if(lstm):
        print(notes_clip_str)
        chord_list = get_neural_chords(notes_clip_str)
        print(chord_list)
        chord_list = chord_list[len(chord_clip):]
        print(chord_list)
    else:
        chord_list = get_chord_list(chord_clip)
    count = 0
    for root in chord_list:
        note_midi = librosa.note_to_midi(root) - 24
        count = count +1
        #分解和弦
        if(style==1 and count <=len(chord_list)-2 and count!=1):
            track.append(mido.Message('note_on', note=note_midi, velocity=90, time=0))
            track.append(mido.Message('note_on', note=note_midi + 4, velocity=90, time=240))
            track.append(mido.Message('note_on', note=note_midi + 7, velocity=90, time=240))
            track.append(mido.Message('note_on', note=note_midi + 12, velocity=90, time=240))

            track.append(mido.Message('note_off', note=note_midi, velocity=90, time=240))
            track.append(mido.Message('note_off', note=note_midi + 4, velocity=90, time=0))
            track.append(mido.Message('note_off', note=note_midi + 7, velocity=90, time=0))
            track.append(mido.Message('note_off', note=note_midi + 12, velocity=90, time=0))
        # 柱式和弦
        else:
            track.append(mido.Message('note_on', note=note_midi, velocity=velocity,time=0))
            track.append(mido.Message('note_on', note=note_midi +4, velocity=velocity,time=0))
            track.append(mido.Message('note_on', note=note_midi+7, velocity=velocity,time=0))

            track.append(mido.Message('note_off', note=note_midi, velocity=velocity,time=960))
            track.append(mido.Message('note_off', note=note_midi +4, velocity=velocity,time=0))
            track.append(mido.Message('note_off', note=note_midi+7, velocity=velocity,time=0))
    output_midi = getFilename(output_midi, 222)
    # mid.save(output_midi)
    return mid,output_midi


if __name__ == '__main__':
    pass
    # print("in __main__")
    # note_clip =[[("c4",3),("r",1),("r",6),("d4",5),("c4",3)],
    #              [("d4", 6), ("r", 1), ("r",6), ("e4", 5), ("d4", 1)]]
    # roots = get_chord_list(note_clip)
    # print(roots)
