import math
import os
from xlutils.copy import copy
from pandas import DataFrame, read_excel
import xlrd


def aucData(auc_dir, aucPath):
    files = os.listdir(auc_dir)
    files.sort()
    fileNum = 0
    for file_name in files:
        if (file_name[-2:] == 're'):
            continue

        path = os.path.join(auc_dir, file_name)  # 统计文件目录

        workbook = xlrd.open_workbook(aucPath)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        worksheet = workbook.sheet_by_name(sheets[0])  # 获取工作簿中所有表格中的的第s个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第s个表格

        new_worksheet.write(0, fileNum, file_name[-7:])

        data = DataFrame(read_excel(path, sheet_name='data'))
        # 获取总列数
        li = data.columns

        # for da in range(1, len(li)):
        dat = li[len(li) - 1]
        # 获取表格最后一列的数据
        d = data[dat]

        for l in range(0, len(d)):
            new_worksheet.write(l + 1, fileNum, format(float(d[l]), '.4f'))

        new_workbook.save(aucPath)

        fileNum += 1
