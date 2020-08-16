from agent_Model import RNNnet
from agentDataSet import agentData
from MLP_utils import _test, _train
from save_Model import saveModel

from torch.utils.data import DataLoader

if __name__ == '__main__':
    # linear_net = LinearNet([18, 32, 64, 32, 3])
    rnn_net = RNNnet(3, 64, 2, 2)

    train_data = agentData('../allData/data')
    test_data = agentData('../allData/test')

    train_dl = DataLoader(train_data, 1, shuffle=False)
    test_dl = DataLoader(test_data, 1, shuffle=False)

    jishu = 0
    accuracyRateMax = 0.0
    for i in _train(rnn_net, train_dl, 80, 0.001):
        jishu += 1
        if jishu % 1 == 0:
            accuracyRate = _test(rnn_net, test_data)
            if accuracyRate > accuracyRateMax:
                accuracyRateMax = accuracyRate
                saveModel(rnn_net)
    print('=============================================================================')
