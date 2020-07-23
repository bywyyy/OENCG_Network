from dataProvider import get_file_data
from dataProvider import agentArray
import xlrd
from xlutils.copy import copy
import os


def agentData(data_dir):
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

        for num in range(0, dataLen):
            if (data_list[num][1] == 0):
                labelNum += 1  # 达成一致次数+1
                accProvide[data_list[num][0]] += 1  # agent提议次数+1
                accNumber[data_list[num][0]] += 1  # agent提议被接受次数+1
                avePayoff[agent1] += data_list[num][3]  # agent总收益增加
                avePayoff[agent2] += data_list[num][4]
                avePayoff[agent3] += data_list[num][5]

            else:
                accProvide[data_list[num][0]] += 1  # agent提议次数+1

        from main import dataPath
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
        worksheet = workbook.sheet_by_name(sheets[sheetNumber])
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(sheetNumber)  # 获取转化后工作簿中的表格

        # 计算agent平均收益
        for index1 in range(0, agentLen):
            if (avePayoff[index1] != 0):
                avePayoff[index1] /= labelNum
                new_worksheet.write(rows_old, index1 * 2, format(avePayoff[index1]))

        # 计算agent提议被接受次数
        for index2 in range(0, agentLen):
            if (accProvide[index2] != 0):
                accProbability[index2] = accNumber[index2] / accProvide[index2]
                new_worksheet.write(rows_old, index2 * 2 + 1, format(accProbability[index2]))
        new_workbook.save(dataPath)
