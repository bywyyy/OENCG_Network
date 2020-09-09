from agent_Modelmlpnw import LinearNet
from agentDataSet import agentData
from MLP_utils import _test, _train
from save_Model import saveModel
from torch.utils.data import DataLoader

# se = [1, 2, 4, 7, 11, 16, 21]
# se = [4]
se = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21]

if __name__ == '__main__':

    cnt = 0
    for k in se:
        cnt += 1
        linear_net = LinearNet(k)

        train_data = agentData('../allData/data', k)
        test_data = agentData('../allData/test', k)

        train_dl = DataLoader(train_data, 16, shuffle=True)
        test_dl = DataLoader(test_data, 1, shuffle=False)

        jishu = 0
        accuracyRateMax = 0.0
        for i in _train(linear_net, train_dl, 30, 3e-5):
            jishu += 1
            if jishu % 1 == 0:
                accuracyRate = _test(linear_net, test_data, k)
                if accuracyRate > accuracyRateMax:
                    accuracyRateMax = accuracyRate
                    saveModel(linear_net)

        print('=============================================================================')
