import numpy as np
import torch
from torch.autograd import Variable


def testCNN(model, dst, playerNum):
    model.eval()

    maxPayoff = 0.0
    recpayoff = [0] * 3
    for i in range(0, dst.__len__()):
        data = dst[i]
        data = torch.Tensor([data.numpy()])
        data = Variable(data.float(), requires_grad=False)
        # data = Variable(torch.unsqueeze(data, dim=1).float(), requires_grad=False)

        with torch.no_grad():
            outputs = model(data)
        outputs2 = outputs[0].detach().numpy()
        np.set_printoptions(precision=6, suppress=True)

        payoff = data.numpy()
        payoff2 = payoff[0][1]
        payoff3 = list(map(int, payoff2[-3:] * 100))
        print("payoff: {}, outputs : {},".format(payoff3, outputs2))

        # if (estCoalition(payoff3)):
        asp = payoff3[playerNum] / 100 * outputs2[0]
        # 选择提议者收益*被接受概率更高的收益（乘积相同，但提议者收益更高时也更新）
        if asp == maxPayoff and payoff3[playerNum] > recpayoff[playerNum]:
            recpayoff = payoff3[:]
            maxPayoff = asp

        elif asp > maxPayoff:
            recpayoff = payoff3[:]
            maxPayoff = asp

        print("recommand payoff: {}, aspiration : {},".format(recpayoff, maxPayoff))

    print(recpayoff[0].__str__() + ',' + recpayoff[1].__str__() + ',' + recpayoff[2].__str__())


def testMLP(model, dst, playerNum):
    model.eval()

    maxPayoff = 0.0
    recpayoff = [0] * 3
    for i in range(0, dst.__len__()):
        data = dst[i]
        data = torch.Tensor([data.numpy()])

        with torch.no_grad():
            outputs = model(data)
        outputs2 = outputs[0].detach().numpy()
        np.set_printoptions(precision=6, suppress=True)

        payoff = data.numpy()
        payoff2 = payoff[0]
        if (payoff2[-1] >= 1):
            payoff3 = list(map(int, payoff2[-6:-3] * 100))
        else:
            payoff3 = list(map(int, payoff2[-3:] * 100))
        print("payoff: {}, outputs : {},".format(payoff3, outputs2))

        # if (estCoalition(payoff3)):
        asp = payoff3[playerNum] / 100 * outputs2[0]
        # 选择提议者收益*被接受概率更高的收益（乘积相同，但提议者收益更高时也更新）
        if asp == maxPayoff and payoff3[playerNum] > recpayoff[playerNum]:
            recpayoff = payoff3[:]
            maxPayoff = asp

        elif asp > maxPayoff:
            recpayoff = payoff3[:]
            maxPayoff = asp

        print("recommand payoff: {}, aspiration : {},".format(recpayoff, maxPayoff))

    print(recpayoff[0].__str__() + ',' + recpayoff[1].__str__() + ',' + recpayoff[2].__str__())
