from pandas import DataFrame, read_excel
import xlrd
import xlwt
import math
from xlutils.copy import copy


def colAverage(path, name, s):
    # path:excel文件路径
    # name:sheetName,表名

    workbook = xlrd.open_workbook(path)  # 打开工作簿
    sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
    new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
    worksheet = workbook.sheet_by_name(sheets[s])  # 获取工作簿中所有表格中的的第s个表格
    rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
    new_worksheet = new_workbook.get_sheet(s)  # 获取转化后工作簿中的第s个表格

    data = DataFrame(read_excel(path, sheet_name=name))
    # 获取总列数
    li = data.columns

    for da in range(1, len(li)):
        dat = li[da]
        # 获取每一列的数据
        d = data[dat]
        num = len(d)
        sum = 0
        for l in range(0, len(d)):
            pieceNum = float(d[l])

            if (math.isnan(pieceNum)):
                num -= 1
                continue
            sum += pieceNum

        if num == 0:
            continue
        aveNumber = sum / num
        new_worksheet.write(rows_old, da, aveNumber)

    new_workbook.save(path)


def colAverageSummary(path, name):
    # path:excel文件路径
    # name:sheetName,表名

    workbook = xlrd.open_workbook(path)  # 打开工作簿
    new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
    worksheet = workbook.sheet_by_name(name)  # 获取工作簿中所有表格中的的第s个表格
    rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
    new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第s个表格

    new_worksheet.write(rows_old + 1, 1, 'cnn-w')
    new_worksheet.write(rows_old + 2, 1, 'cnn-nw')
    new_worksheet.write(rows_old + 3, 1, 'mlp-w')
    new_worksheet.write(rows_old + 4, 1, 'mlp-nw')

    data = DataFrame(read_excel(path, sheet_name=name))
    # 获取总列数
    li = data.columns

    for da in range(2, len(li)):
        dat = li[da]
        # 获取每一列的数据
        d = data[dat]
        num = [0] * 4
        sum = [0] * 4
        for l in range(0, len(d), 4):
            for ci in range(0, 4):
                pieceNum = float(d[l + ci])
                if (math.isnan(pieceNum) == False):
                    num[ci] += 1
                    sum[ci] += pieceNum

        for avgi in range(0, 4):
            if (num[avgi] != 0):
                aveNumber = sum[avgi] / num[avgi]
                new_worksheet.write(rows_old + avgi + 1, da, aveNumber)

    new_workbook.save(path)
