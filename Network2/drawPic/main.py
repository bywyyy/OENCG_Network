from copy import copy
import xlrd
import xlwt
from dataProvider import agentData
from aucStatic import aucData

if __name__ == '__main__':
    w = xlwt.Workbook(encoding='utf-8')  # 新建工作簿
    ws = w.add_sheet('data')  # 新建sheet

    # data_dir = '../../Network2/drawPICdata' #统计文件目录
    # dataPath = '../drawPICdata.xls' #文件名称
    # w.save(dataPath)
    #
    # # 读取xml文件填写表内容
    # agentData(data_dir, dataPath)

    auc_dir = '../../Network2/aucStatic'  # 统计文件目录
    aucPath = '../aucStatic.xls'  # 文件名称
    w.save(aucPath)

    # 读取xml文件填写表内容
    aucData(auc_dir, aucPath)
