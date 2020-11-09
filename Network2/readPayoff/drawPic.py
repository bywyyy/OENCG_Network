import matplotlib.pyplot as plt
import os
from pandas import DataFrame, read_excel

plt.ion()


def drawPicture(dataPath):
    # powerIndexPath = '../powerIndex.xls'  # 文件名称

    path = os.path.join(dataPath)  # 统计文件目录
    data = DataFrame(read_excel(path, sheet_name='data'))
    # datapIndex= DataFrame(read_excel(powerIndexPath, sheet_name='data'))

    # 获取总列数
    li = data.columns
    # 获取表格第i列数据的数据
    player1Weight = data[li[2]]
    player2Weight = data[li[3]]
    player3Weight = data[li[4]]

    player1pIndex = data[li[14]]
    player2pIndex = data[li[15]]
    player3pIndex = data[li[16]]

    player1Payoff = data[li[11]]
    player2Payoff = data[li[12]]
    player3Payoff = data[li[13]]

    player1Weight.append(player2Weight)
    weightData = player1Weight.append(player3Weight)

    player1Payoff.append(player2Payoff)
    payoffData = player1Payoff.append(player3Payoff)

    player1pIndex.append(player2pIndex)
    pIndexData = player1pIndex.append(player3pIndex)

    fig = plt.figure(figsize=(10, 4))
    ax1 = fig.add_subplot(1, 2, 1)
    ax2 = fig.add_subplot(1, 2, 2)
    plt.tight_layout(pad=5)  # 设置图片间距

    ax1.set_xlabel('payoff-ratio')
    ax1.set_ylabel('weight-ratio')
    ax1.scatter(payoffData, weightData, alpha=0.5, edgecolors='white')  # edgecolors = 'w',亦可
    # plt.show()

    ax2.set_xlabel('payoff-ratio')
    ax2.set_ylabel('powerIndex-ratio')
    ax2.scatter(payoffData, pIndexData, alpha=0.5, edgecolors='black')  # edgecolors = 'w',亦可

    plt.pause(0.3)
    plt.savefig('../scatterWeightRate.png')
    plt.close()
