import xlrd
import xlwt
from xlutils.copy import copy
from agentDataSet import agentData
from columnAverage import colAverage
from columnAverage import colAverageSummary
import sys
import importlib

importlib.reload(sys)

import time

agentArray = ['AgentTS', 'AgentTS2', 'AgentNetwork', 'AgentTraverse', 'Agent', 'Human', 'AgentTraverse2',
              'ReinforceAgent', 'AgentProselfA', 'AgentProselfB']

timen = time.strftime("%d%H%S")
# timen = '302249'

dataPath = ['../stacnn-w' + timen + '.xls', '../stacnn-nw' + timen + '.xls', '../stamlp-w' + timen + '.xls',
            '../stamlp-nw' + timen + '.xls']

data_dir = ['../../Network2/allData/stacnnw', '../../Network2/allData/stacnnnw', '../../Network2/allData/stamlpw',
            '../../Network2/allData/stamlpnw']

sheetName = ['1-1-1', '1-1-2', '1-2-2', '2-2-3', '3-3-5', '2-3-3', '2-3-4', '3-3-4', '4-4-5', '4-4-6', '4-5-6', '4-5-7',
             '5-5-7', '4-7-7', '5-7-9']

if __name__ == '__main__':

    #读取xml文件写入excel
    for p in range(0, 4):

        w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
        for weight in sheetName:
            ws = w.add_sheet(weight)  # 新建sheet

        w.save(dataPath[p])

        # 为每个sheet写入表头
        workbook = xlrd.open_workbook(dataPath[p])  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象

        for i in range(0, len(sheetName)):
            worksheet = workbook.sheet_by_name(sheets[i])  # 获取工作簿中所有表格中的的第i个表格
            rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
            new_worksheet = new_workbook.get_sheet(i)  # 获取转化后工作簿中的第一个表格
            arrNum = 0
            for item in agentArray:
                new_worksheet.write(rows_old, arrNum * 2 + 1, agentArray[arrNum])
                arrNum += 1

            new_workbook.save(dataPath[p])

        # 读取xml文件填写表内容
        agentData(data_dir[p], dataPath[p])

        # 计算每列平均值
        for s in range(0, len(sheetName)):
            colAverage(dataPath[p], sheetName[s], s)

    # 汇总文件
    w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
    ws = w.add_sheet('summary')  # 新建sheet
    dataPathSummary = '../summary' + timen + '.xls'
    w.save(dataPathSummary)
    workbook = xlrd.open_workbook(dataPathSummary)  # 打开工作簿
    sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
    new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
    new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格
    # 填写前两列表头
    for si in range(0, len(sheetName)):
        new_worksheet.write(si * 4 + 1, 0, sheetName[si])
        new_worksheet.write(si * 4 + 1, 1, 'cnn_w')
        new_worksheet.write(si * 4 + 2, 1, 'cnn_nw')
        new_worksheet.write(si * 4 + 3, 1, 'mlp_w')
        new_worksheet.write(si * 4 + 4, 1, 'mlp_nw')
    # 填写第一列表头
    for ai in range(0, len(agentArray)):
        new_worksheet.write(0, ai * 2 + 2, agentArray[ai])

    new_workbook.save(dataPathSummary)

    # 填写表格内容
    for fi in range(0, len(dataPath)):
        workbook = xlrd.open_workbook(dataPathSummary)  # 打开工作簿
        sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
        new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象
        new_worksheet = new_workbook.get_sheet(0)  # 获取转化后工作簿中的第一个表格

        workbook = xlrd.open_workbook(dataPath[fi], 'rb')  # 打开工作簿
        # sheet循环
        for shi in range(0, len(sheetName)):
            sheet = workbook.sheet_by_name(sheetName[shi])
            rows = sheet.nrows  # 获取表格中已存在的数据的行数
            # agent数据循环
            for agi in range(0, len(agentArray)):
                offeravg = str(sheet.cell_value(rows - 1, agi * 2 + 1))
                accavg = str(sheet.cell_value(rows - 1, (agi + 1) * 2))
                new_worksheet.write(shi * 4 + 1 + fi, (agi + 1) * 2, format(offeravg))
                new_worksheet.write(shi * 4 + 1 + fi, (agi + 1) * 2 + 1, format(accavg))

                new_workbook.save(dataPathSummary)
    colAverageSummary(dataPathSummary, 'summary')
