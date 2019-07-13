#Author 张新豪
#from django.shortcuts import render
from .humToMelody import humToMelody as humToMelody
# from .humToMelody2 import humToMelody2 as humToMelody2
from django.http import HttpResponse, Http404
import _thread
import requests
import json

# Create your views here.

#哼唱转旋律和智能伴奏合成的一个接口，伴奏若isBass,isDum，isChord为空则是单纯的旋律
def HumToMelody(input_wav, instrument,token,fid,rid,isBass,isDrum,isChord):
    # if(isChord):
    #     output_midi = humToMelody(input_wav,instrument,isBass,isDrum,isChord)
    # else:
    #     output_midi = humToMelody2(input_wav, instrument, isBass, isDrum, isChord)
    output_midi = humToMelody(input_wav, instrument, isBass, isDrum, isChord)
    data = {'fid': fid,'token':token,'rid':rid,'filePath':output_midi}
    requests.post('http://localhost:8080/api/external/completeTask',data = data)
    print(output_midi)

#开一个线程跑哼唱转旋律
def getMelody(request):
    input_wav = "/home/songhut/"+request.POST['filePath']
    instrument = int(request.POST['type'])
    token = request.POST['token']
    fid = int(request.POST['fid'])
    rid = int(request.POST['rid'])
    isBass = int(request.POST['isBass'])
    isDrum = int(request.POST['isDrum'])
    isChord = int(request.POST['isChord'])
    _thread.start_new_thread(HumToMelody,(input_wav,instrument,token,fid,rid,isBass,isDrum,isChord))
    return  HttpResponse("start transfering!")
