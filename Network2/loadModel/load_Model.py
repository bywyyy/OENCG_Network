from test import testMLP, testCNN
from agentDataSetcnw import agentDatacnw
from agentDataSetcw import agentDatacw
from agentDataSetmnw import agentDatamnw
from agentDataSetmw import agentDatamw
import torch

testpath = '/Users/linjie/PycharmProjects/OENCG_Network/zsnp/log/agentLearn'

# testpath = '/Users/linjie/PycharmProjects/OENCG_Network/Network2/allData/testTest'


def loadModel(k, playerNum, network):
    '''
        参数1：K值
        参数2：playerNum
        参数3：网络类型。1:MLP无权重,2:MLP有权重,3:CNN无权重,4:CNN有权重
        '''
    if network == 1:
        model = torch.load('/Users/linjie/PycharmProjects/OENCG_Network/Network2/params/mlp_nwParams' + str(k) + '.pth')
        test_data = agentDatamnw(testpath, k, playerNum)
        testMLP(model, test_data, playerNum)

    elif network == 2:
        model = torch.load('/Users/linjie/PycharmProjects/OENCG_Network/Network2/params/mlp_wParams' + str(k) + '.pth')
        test_data = agentDatamw(testpath, k, playerNum)
        testMLP(model, test_data, playerNum)

    elif network == 3:
        model = torch.load('/Users/linjie/PycharmProjects/OENCG_Network/Network2/params/cnn_nwParams' + str(k) + '.pth')
        test_data = agentDatacnw(testpath, k, playerNum)
        testCNN(model, test_data, playerNum)

    elif network == 4:
        model = torch.load('/Users/linjie/PycharmProjects/OENCG_Network/Network2/params/cnn_wParams' + str(k) + '.pth')
        test_data = agentDatacw(testpath, k, playerNum)
        testCNN(model, test_data, playerNum)

    # model = CNNnet()
    # model.load_state_dict(torch.load('./params/cnn_nwParams' + k + '.pth'))

    # print('========================' + str(accuracyRate) + '=====================================================')
