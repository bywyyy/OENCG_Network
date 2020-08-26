from copy import copy
import xlrd
import xlwt
from dataProvider import agentData

if __name__ == '__main__':
    # 读取xml文件写入excel

    w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
    ws = w.add_sheet('data')  # 新建sheet

    data_dir = '../../Network2/drawPICdata' #统计文件目录
    dataPath = '../drawPICdata.xls' #文件名称
    w.save(dataPath)

    # 读取xml文件填写表内容
    agentData(data_dir, dataPath)
