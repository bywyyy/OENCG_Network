from test import _testCNN,_testMLP
from agentDataSetcnw import agentDatacnw
from agentDataSetcw import agentDatacw
from agentDataSetmnw import agentDatamnw
from agentDataSetmw import agentDatamw
import torch


def loadModel(int):
    # model = CNNnet()
    # model.load_state_dict(torch.load('./params/cnn_nwParams' + int + '.pth'))

    # model = torch.load('../params/cnn_nwParams' + str(int) + '.pth')
    # test_data = agentDatacnw('../allData/testTest', int)

    # model = torch.load('../params/cnn_wParams' + str(int) + '.pth')
    # test_data = agentDatacw('../allData/testTest', int)

    # model = torch.load('../params/mlp_nwParams' + str(int) + '.pth')
    # test_data = agentDatamnw('../allData/testTest', int)

    model = torch.load('../params/mlp_wParams' + str(int) + '.pth')
    test_data = agentDatamw('../allData/testTest', int)


    # accuracyRate = _testCNN(model, test_data)
    accuracyRate = _testMLP(model, test_data)


    print('========================' + str(accuracyRate) + '=====================================================')
