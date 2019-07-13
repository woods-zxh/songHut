#Author 张新豪
from midi2audio import  FluidSynth

def wav_filename(midi_filename):
    wav = midi_filename[:-3]+"wav"
    return wav

def midi2wav(midi_filename):
    fs = FluidSynth(sound_font = "./music/a.sf2")

    wav = wav_filename(midi_filename)
    fs.midi_to_audio(midi_filename, wav)
    return wav

if __name__ == '__main__':
    midi_filename ="/home/songhut/E:/data/4/1/xiaoxingxing_1_222_32_32_32.mid"
    fs = FluidSynth(sound_font="a.sf2")
    wav = wav_filename(midi_filename)
    fs.midi_to_audio(midi_filename, wav)
    print("hello")
    print(wav)
#
# wav = wav_filename("./123/qwe/231/qewq.mid")
# print(wav)

