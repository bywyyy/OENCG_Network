from agent_Model import CNNnet
from test import _test
from agentDataSet import agentData
import torch


def saveModel(model):
    from agent_Model import timen
    torch.save(model, '../params/cnn_nnnParams' + timen + '.pth')

def loadModel(int):
    # model = CNNnet()
    # model.load_state_dict(torch.load('./params/cnn_nnnParams' + int + '.pth'))
    model = torch.load('../params/cnn_nnnParams' + str(int) + '.pth')
    test_data = agentData('../allData/testTest', int)
    accuracyRate = _test(model, test_data)
    print('========================' + str(accuracyRate) + '=====================================================')
