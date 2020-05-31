from agent_Model import CNNnet
from agent_Model import setnum
from agentDataSet import agentData
from MLP_utils import _test, _train

from torch.utils.data import DataLoader

se = [1, 3, 6, 10, 15, 20]
point = [16, 48, 80, 128, 192, 256]

globalk = se[0]
def setglobalk(intk):
    global globalk
    globalk = intk

if __name__ == '__main__':
    cnt = 0
    for k in se:
        setnum(point[cnt])
        setglobalk(se[cnt])
        cnt += 1
        cnn_net = CNNnet()

        train_data = agentData('../data', k)
        test_data = agentData('../test', k)

        train_dl = DataLoader(train_data, 2, shuffle=False)
        test_dl = DataLoader(test_data, 1, shuffle=False)

        jishu = 0
        for i in _train(cnn_net, train_dl, 200, 0.0001):
            jishu += 1
            if jishu % 10 == 0:
                _test(cnn_net, test_data)

    print('=============================================================================')
