from test import testMLP,testCNN
# from agentDataSetcnw import agentDatacnw
# from agentDataSetcw import agentDatacw
from agentDataSetmnw import agentDatamnw
# from agentDataSetmw import agentDatamw
import torch


# import sys
# print(sys.path)


def loadModel(k, playerNum):
    # model = CNNnet()
    # model.load_state_dict(torch.load('./params/cnn_nwParams' + k + '.pth'))

    # model = torch.load('../params/cnn_nwParams' + str(k) + '.pth')
    # test_data = agentDatacnw('../allData/testTest', k)

    # model = torch.load('../params/cnn_wParams' + str(k) + '.pth')
    # test_data = agentDatacw('../allData/testTest', k)

    model = torch.load('/Users/linjie/PycharmProjects/OENCG_Network/Network2/params/mlp_nwParams' + str(k) + '.pth')
    testpath = '/Users/linjie/PycharmProjects/OENCG_Network/zsnp/log/agentLearn'
    # testpath = '/Users/linjie/PycharmProjects/OENCG_Network/Network2/allData/testTest'
    test_data = agentDatamnw(testpath, k, playerNum)

    # model = torch.load('../params/mlp_wParams' + str(k) + '.pth')
    # test_data = agentDatamw('../allData/testTest', k, playerNum)

    # accuracyRate = testCNN(model, test_data)
    testMLP(model, test_data, playerNum)

    # print('========================' + str(accuracyRate) + '=====================================================')

    # return accuracyRate
