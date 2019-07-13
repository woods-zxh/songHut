#Author 张新豪
#此一些工具函数

#获得正确的文件
def getFilename(input_wav,instrument):
    list1 = input_wav.split('\\')
    output = ""
    count = 0
    for str1 in list1:
        if(count<(len(list1)-1)):
            output = output + str1+"\\"
            count = count + 1
        else:
            filename = str1.split('.')[0]
            output = output +filename +"_"+str(instrument)+"."+"mid"
            count = count + 1
    return output

#获得键
def get_key(dict, value):
    return [k for k, v in dict.items() if v == value][0]

if __name__ == '__main__':
    print("in main")
    print(getFilename( ".\wav\liangzhilaohu.wav",40))