from test import _test
from agentDataSet import agentData
import torch


def saveModel(model):
    from agent_Modelcnnw import timen
    torch.save(model, '../params/cnn_wParams' + timen + '.pth')

def loadModel(int):
    # model = CNNnet()
    # model.load_state_dict(torch.load('./params/cnn_nwParams' + int + '.pth'))
    model = torch.load('../params/cnn_wParams' + str(int) + '.pth')
    test_data = agentData('../allData/testTest', int)
    accuracyRate = _test(model, test_data)
    print('========================' + str(accuracyRate) + '=====================================================')
