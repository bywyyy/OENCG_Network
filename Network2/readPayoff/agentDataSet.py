from xlutils.copy import copy
from dataProvider import get_file_data
import xlrd
from drawPic import drawPicture
import os
from pandas import DataFrame, read_excel


def agentData(data_dir, dataPath, powerIndexPath):
    files = os.listdir(data_dir)
    files.sort()
    for file_name in files:
        if (file_name[-2:] == 're'):
            continue

        path = os.path.join(data_dir, file_name)  # 统计文件目录

        data_list = get_file_data(path)

        piece_weight = []
        piece_weight.append(data_list[0][6])
        piece_weight.append(data_list[0][7])
        piece_weight.append(data_list[0][8])
        weightSum = data_list[0][6] + data_list[0][7] + data_list[0][8]
        weight = '-'.join(str(x) for x in piece_weight)  # 将game权重转化为string

        piecePayoff = [0.0] * 3
        sucProposalNum = 0  # 成功达成提议的个数
        for i in range(0, len(data_list)):
            if (data_list[i][9] == 0):
                piecePayoff[0] += data_list[0][3] * 1.0
                piecePayoff[1] += data_list[0][4] * 1.0
                piecePayoff[2] += data_list[0][5] * 1.0
                sucProposalNum += 1

        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第s个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第s个表格

        new_worksheet.write(rows_old, 0, file_name)  # 写入读取xml文件的名称
        new_worksheet.write(rows_old, 1, weight)  # 写入game权重
        new_worksheet.write(rows_old, 2, format(piece_weight[0] / weightSum, '.3f'))  # 写入权重比例
        new_worksheet.write(rows_old, 3, format(piece_weight[1] / weightSum, '.3f'))
        new_worksheet.write(rows_old, 4, format(piece_weight[2] / weightSum, '.3f'))

        new_worksheet.write(rows_old, 5, data_list[0][0])  # 写入agentName
        new_worksheet.write(rows_old, 6, data_list[0][1])
        new_worksheet.write(rows_old, 7, data_list[0][2])

        payoffSum = 0
        # 写入payoff
        for t in range(0, len(piecePayoff)):
            payoffSum += piecePayoff[t]
            if (sucProposalNum != 0):
                new_worksheet.write(rows_old, 8 + t, format(piecePayoff[t] / sucProposalNum, '.3f'))
            else:
                new_worksheet.write(rows_old, 8 + t, format(piecePayoff[t], '.3f'))

        # 写入payoff所占比例
        for p in range(0, 3):
            if (payoffSum != 0):
                new_worksheet.write(rows_old, 11 + p, format(piecePayoff[p] / payoffSum, '.3f'))
            else:
                new_worksheet.write(rows_old, 11 + p, format(piecePayoff[p], '.3f'))

        # 写入相关powerIndex
        dataPowerIndex = DataFrame(read_excel(powerIndexPath, sheet_name='Sheet1'))
        li = dataPowerIndex.columns

        # 获取表格第0列数据的数据
        player1powerIndex = dataPowerIndex[li[0]]
        for e in range(0, len(player1powerIndex)):
            if (weight == player1powerIndex[e]):
                for c in range(1, 10):
                    new_worksheet.write(rows_old, 13 + c, format(dataPowerIndex.iloc[e, c], '.3f'))

        new_workbook.save(dataPath)

    drawPicture(dataPath)
