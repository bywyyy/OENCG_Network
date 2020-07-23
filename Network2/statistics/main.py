import xlrd
import xlwt
from xlutils.copy import copy
from agentDataSet import agentData
import time

agentArray = ['AgentTS', 'AgentTS2', 'AgentTraverse', 'Agent', 'Human', 'AgentTraverse2', 'ReinforceAgent',
              'AgentProselfA', 'AgentProselfB']

timen = time.strftime("%d%H%M%S")
dataPath = '../statistic' + timen + '.xls'

data_dir = '../../Network2/allData/data'

if __name__ == '__main__':
    w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
    ws = w.add_sheet('1-1-1')  # 新建sheet
    ws2 = w.add_sheet('1-1-2')
    ws3 = w.add_sheet('1-2-2')
    ws4 = w.add_sheet('2-2-3')
    ws5 = w.add_sheet('3-3-5')

    w.save(dataPath)

    #为每个sheet写入表头
    workbook = xlrd.open_workbook(dataPath)  # 打开工作簿
    sheets = workbook.sheet_names()  # 获取工作簿中的所有表格
    new_workbook = copy(workbook)  # 将xlrd对象拷贝转化为xlwt对象

    for i in range(0, 5):
        worksheet = workbook.sheet_by_name(sheets[i])  # 获取工作簿中所有表格中的的第i个表格
        rows_old = worksheet.nrows  # 获取表格中已存在的数据的行数
        new_worksheet = new_workbook.get_sheet(i)  # 获取转化后工作簿中的第一个表格
        arrNum = 0
        for item in agentArray:
            new_worksheet.write(rows_old, arrNum * 2, agentArray[arrNum])
            arrNum += 1

        new_workbook.save(dataPath)

    agentData(data_dir)
