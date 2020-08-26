import math
import os
from xlutils.copy import copy
from pandas import DataFrame, read_excel
import xlrd


def agentData(data_dir, dataPath):
    files = os.listdir(data_dir)
    files.sort()
    for file_name in files:
        if (file_name[-2:] == 're'):
            continue

        path = os.path.join(data_dir, file_name)  # 统计文件目录

        # for filename in file_path:
        #     path = '../../Network2/drawPICdata/' + filename

        workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第s个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第s个表格

        data = DataFrame(read_excel(path, sheet_name='data'))
        # 获取总列数
        li = data.columns

        # for da in range(1, len(li)):
        dat = li[len(li) - 1]
        # 获取表格最后一列的数据
        d = data[dat]
        num = len(d)
        sum = 0
        for l in range(0, len(d)):
            pieceNum = float(d[l])

            if (math.isnan(pieceNum)):
                num -= 1
                continue
            elif (pieceNum < 0.5):
                num -= 1
                continue
            sum += pieceNum

        aveNumber = sum / num

        new_worksheet.write(rows_old, 0, file_name)
        new_worksheet.write(rows_old, 1, aveNumber)

        new_workbook.save(dataPath)
