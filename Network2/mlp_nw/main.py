from agent_Modelmlpnw import LinearNet, setnum
from agentDataSet import agentData
from MLP_utils import _test, _train
from save_Model import saveModel
from torch.utils.data import DataLoader

# se = [1, 2, 4, 7, 11, 16, 21]
se = [21]

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
        accuracyRateMax = 0.0
        for i in _train(linear_net, train_dl, 80, 0.002):
            jishu += 1
            if jishu % 1 == 0:
                accuracyRate = _test(linear_net, test_data)
                if accuracyRate > accuracyRateMax:
                    accuracyRateMax = accuracyRate
                    saveModel(linear_net)

        print('=============================================================================')
