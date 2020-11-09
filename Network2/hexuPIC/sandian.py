import numpy as np
import matplotlib.pyplot as plt
from mpl_toolkits.mplot3d import Axes3D


def sandian():
    data = np.random.randint(0, 255, size=[40, 40, 40])

    x, y = data[0], data[1]
    ax = plt.subplot(111)  # 创建一个三维的绘图工程
    #  将数据点分成三部分画，在颜色上有区分度
    # ax.scatter(x[:10], y[:10], c='y' )  # 绘制数据点
    # ax.scatter(x[10:20], y[10:20], c='r')
    # ax.scatter(x[30:40], y[30:40], c='g')
    plt.plot(0.08, 0.34, 'o', c='#0075BB', label="Loki", markersize=10)
    plt.plot(0.32, 0.43, 'o', c='#535353', label="Rhett", markersize=10)
    plt.plot(0.17, 0.37, 'o', c='#D45200', label="Odin", markersize=10)
    plt.plot(0.62, 0.49, 'o', c='#C78F00', label="Elsa", markersize=10)

    plt.plot(0.08, 0.3, 's', c='#0075BB', label="Loki'", markersize=10)
    plt.plot(0.32, 0.39, 's', c='#535353', label="Rhett'", markersize=10)
    plt.plot(0.17, 0.30, 's', c='#D45200', label="Odin'", markersize=10)
    plt.plot(0.59, 0.43, 's', c='#C78F00', label="Elsa'", markersize=10)
    plt.axis([0, 1, 0, 0.8])
    plt.title('Comprehensive Assessment')

    # 坐标轴
    plt.ylabel('Effectiveness Index')
    plt.xlabel('Character Index')
    # plt.legend(numpoints=1)
    plt.legend(loc='upper right', fontsize=10)  # 标签位置
    plt.xlim(0, 1)
    plt.show()
