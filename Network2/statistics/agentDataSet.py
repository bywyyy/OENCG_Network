from dataProvider import get_file_data
from dataProvider import agentArray
import xlrd
from xlutils.copy import copy
import os


def agentData(data_dir, dataPath):
    files = os.listdir(data_dir)
    for file_name in files:
        if (file_name[-2:] == 're'):
            continue

        file_path = os.path.join(data_dir, file_name)

        data_list = get_file_data(file_path)
        weightNum = data_list[0][6] + data_list[0][7] + data_list[0][8]  # 游戏权重和
        agent1 = data_list[0][9]
        agent2 = data_list[0][10]
        agent3 = data_list[0][11]

        dataLen = data_list.__len__()
        agentLen = len(agentArray)
        avePayoff = [0] * agentLen  # agent平均收益
        accProbability = [0] * agentLen
        accProvide = [0] * agentLen  # agent提议次数
        accNumber = [0] * agentLen  # agent提议被接受次数
        labelNum = 0  # 达成一致次数

        roundNum = 0  # stage达成一致所用round数
        roundSum = 0  # round和

        for num in range(0, dataLen):
            if (data_list[num][1] == 0):
                labelNum += 1  # 达成一致次数+1
                accProvide[data_list[num][0]] += 1  # agent提议次数+1
                accNumber[data_list[num][0]] += 1  # agent提议被接受次数+1
                avePayoff[agent1] += data_list[num][3]  # agent总收益增加
                avePayoff[agent2] += data_list[num][4]
                avePayoff[agent3] += data_list[num][5]

                roundNum += 1  # 达成协议所用round+1
                roundSum += roundNum  # 计算round和
                roundNum = 0

            else:
                accProvide[data_list[num][0]] += 1  # agent提议次数+1
                roundNum += 1

        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        sheetNumber = 0
        if (weightNum == 3):
            sheetNumber = 0  # 获取工作簿中所有表格中的的表格
        elif (weightNum == 4):
            sheetNumber = 1
        elif (weightNum == 5):
            sheetNumber = 2
        elif (weightNum == 7):
            sheetNumber = 3
        elif (weightNum == 11):
            sheetNumber = 4

        elif (weightNum == 8):
            sheetNumber = 5
        elif (weightNum == 9):
            sheetNumber = 6
        elif (weightNum == 10):
            sheetNumber = 7
        elif (weightNum == 13):
            sheetNumber = 8
        elif (weightNum == 14):
            sheetNumber = 9
        elif (weightNum == 15):
            sheetNumber = 10
        elif (weightNum == 16):
            sheetNumber = 11
        elif (weightNum == 17):
            sheetNumber = 12
        elif (weightNum == 18):
            sheetNumber = 13
        elif (weightNum == 21):
            sheetNumber = 14

        worksheet = workbook.sheet_by_name(sheets[sheetNumber])
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(sheetNumber)  # 获取转化后工作簿中的表格
        new_worksheet.write(rows_old, 0, format(file_name))

        # 计算agent平均收益
        for index1 in range(0, agentLen):
            if (avePayoff[index1] != 0):
                avePayoff[index1] /= labelNum
                new_worksheet.write(rows_old, index1 * 2 + 1, format(avePayoff[index1]))

        # 计算agent提议被接受次数
        for index2 in range(0, agentLen):
            if (accProvide[index2] != 0):
                accProbability[index2] = accNumber[index2] / accProvide[index2]
                new_worksheet.write(rows_old, (index2 + 1) * 2, format(accProbability[index2]))

        # 计算Game的stage达成协议的平均round
        aveRound = roundSum / 50
        new_worksheet.write(rows_old, 20, format(aveRound))

        new_workbook.save(dataPath)
