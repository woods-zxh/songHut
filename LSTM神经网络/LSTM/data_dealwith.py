#Author 张新豪
#data_dealwith.py 用于数据的预处理
import os

chord2input = {"c":"cccccccc","C":"CCCCCCCC","d":"dddddddd","D":"DDDDDDDD","e":"eeeeeeee","f":"ffffffff","F":"FFFFFFFF","g":"gggggggg","G":"GGGGGGGG","a":"aaaaaaaa","A":"AAAAAAAA","b":"bbbbbbbb"}
note2input = {"0":"r","1":"c","t":"C","2":"d","y":"D","3":"e","4":"f","u":"F","5":"g","i":"G","6":"a","o":"A","7":"b"}

def chord_add(note):
	if note == "c":return "C"
	elif note == "C":return "d"
	elif note == "d":return "D"
	elif note == "D":return "e"
	elif note == "e":return "f"
	elif note == "f":return "F"
	elif note == "F":return "g"
	elif note == "g":return "G"
	elif note == "G":return "a"
	elif note == "a":return "A"
	elif note == "A":return "b"
	elif note == "b":return "c"
	else:return "r"

def get_sum_data(melody_path,chord_path):

	path= melody_path  #待读取的文件夹
	path_list=os.listdir(path)
	path_list.sort() #对读取的路径进行排序

	#按照顺序打开两个文件夹下的旋律数据
	notes = ""
	for filename in path_list:
		real_path = os.path.join(path,filename)
		with open(	real_path , 'r') as f:
			text1 = f.read()  # 字符串
			text = text1.replace("\n","").replace(" ","")
			text2 = text.lower()
			# text3 = note2input[text2]
			notes = notes + text2

	note_inputs = [""]
	note_input = ""

	#进行第一次映射，将原始数据映射到输入数据
	for note in notes:
		note_input = note_input + note2input[note]
	note_inputs[0] = note_input

	#进行数据增强，以半音为单位，对旋律和对应和弦进行升调，对旋律与和弦的对应关系完全没有影响
	#经过数据增强，数据量是原来的12倍
	for i in range(11):
		note_input1 = ""
		for note in note_inputs[i]:
			note = chord_add(note)
			note_input1 = note_input1 + note
		note_inputs.append(note_input1)
		print(note_inputs[i])

	note_input_sum = ""
	for i in range(12):
		note_input_sum = note_input_sum + note_inputs[i]
	print(note_input_sum)

	print("----------------------------------------------------------------------")
	#按照顺序打开两个文件夹下的和弦数据
	path=chord_path  #待读取的文件夹
	path_list=os.listdir(path)
	path_list.sort() #对读取的路径进行排序
	chords = ""
	for filename in path_list:
		# print(os.path.join(path,filename))
		real_path = os.path.join(path,filename)
		with open(	real_path , 'r') as f:
			text1 = f.read()  # 字符串
			text = text1.replace("\n","").replace(" ","")
			text2 = text.lower()
			chords= chords + text2

	chord_inputs = [""]
	chord_input = ""
	for chord in chords:
		chord_input = chord_input + chord2input[chord]
	chord_inputs[0] = chord_input

	#进行数据增强，以半音为单位，对旋律和对应和弦进行升调，对旋律与和弦的对应关系完全没有影响
	#经过数据增强，数据量是原来的12倍
	for i in range(11):
		chord_input1 = ""
		for note in chord_inputs[i]:
			note = chord_add(note)
			chord_input1 = chord_input1 + note
		chord_inputs.append(chord_input1)
		print(chord_inputs[i])

	chord_input_sum = ""
	for i in range(12):
		chord_input_sum = chord_input_sum + chord_inputs[i]
	print(chord_input_sum)
	return note_input_sum,chord_input_sum

if __name__ == '__main__':
	path1 = "../pitch/data/melody/"  # 待读取的文件夹
	path2="../pitch/data/chord/"  #待读取的文件夹
	print(get_sum_data(path1,path2)[0])
	# print(get_sum_data(path1, path2)[1])
