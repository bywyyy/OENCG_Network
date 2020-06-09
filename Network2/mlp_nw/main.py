from agent_Model import LinearNet, setnum
from agentDataSet import agentData
from MLP_utils import _test, _train

from torch.utils.data import DataLoader

se = [1, 3, 6, 10, 15, 20]
# point = [16, 32, 64, 112, 176, 240]

global globalk
globalk = se[0]
if __name__ == '__main__':

    cnt = 0
    for k in se:
        globalk = k

        setnum(se[cnt])
        cnt += 1
        linear_net = LinearNet()

        train_data = agentData('../allData/data', k)
        test_data = agentData('../allData/test', k)

        train_dl = DataLoader(train_data, 4, shuffle=False)
        test_dl = DataLoader(test_data, 1, shuffle=False)

        jishu = 0
        for i in _train(linear_net, train_dl, 100, 0.002):
            jishu += 1
            if jishu % 1 == 0:
                _test(linear_net, test_data)

        print('=============================================================================')
