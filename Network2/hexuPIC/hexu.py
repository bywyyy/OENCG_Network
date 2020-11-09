import numpy as np
import pandas as pd
import matplotlib.pyplot as plt

def hexu():
    data = [[20.0, 16.8, 35.8, 56.9, 26.0, 14.5, 22.3, 10.0, 12.7, 10.0, 16.0, 17.0, 20.5, 28.5, 50.0, 45.9, 48.7],
            [48.5, 27.0, 51.0, 34.2, 27.9, 27.8, 21.0, 33.0, 24.8, 33.6, 38.3, 31.0, 22.5, 24.5, 37.7, 46.3, 18.9],
            [26.0, 23.0, 37.5, 24.5, 22.0, 34.9, 30.1, 47.6, 27.7, 35.0, 26.0, 23.0, 21.5, 36.5, 30.0, 24.9, 17.8],
            [20.0, 16.8, 35.8, 56.9, 26.0, 14.5, 22.3, 10.0, 12.7, 10.0, 16.0, 17.0, 20.5, 28.5, 50.0, 45.9, 48.7],
            [20.0, 16.8, 35.8, 56.9, 26.0, 14.5, 22.3, 10.0, 12.7, 10.0, 16.0, 17.0, 20.5, 28.5, 50.0, 45.9, 48.7]]

    for i in range(0, len(data)):
        sum = 0.0
        for j in range(0, len(data[i])):
            sum += data[i][j]
        print(sum / len(data[1]))

    data1 = np.transpose(data)

    df = pd.DataFrame(data1, columns=['Human', 'Rhett', 'Loki', 'Elsa', 'Odin'])
    plt.figure(figsize=(6, 4))
    # 创建图表、数据

    f = df.boxplot(sym='o',  # 异常点形状，参考marker
                   vert=True,  # 是否垂直
                   whis=1.5,  # IQR，默认1.5，也可以设置区间比如[5,95]，代表强制上下边缘为数据95%和5%位置
                   patch_artist=False,  # 上下四分位框内是否填充，True为填充
                   meanline=False, showmeans=True,  # 是否有均值线及其形状
                   showbox=True,  # 是否显示箱线
                   showcaps=True,  # 是否显示边缘线
                   showfliers=True,  # 是否显示异常值
                   notch=False,  # 中间箱体是否缺口
                   return_type='dict'  # 返回类型为字典
                   )
    # plt.title('boxplot')
    plt.ylabel("Utility")
    plt.show()
    print(f)

    for box in f['boxes']:
        box.set(color='b', linewidth=1)  # 箱体边框颜色
        # box.set(facecolor='b', alpha=0.5)  # 箱体内部填充颜色
    for whisker in f['whiskers']:
        whisker.set(color='k', linewidth=0.5, linestyle='-')
    for cap in f['caps']:
        cap.set(color='gray', linewidth=2)
    for median in f['medians']:
        median.set(color='DarkBlue', linewidth=2)
    for flier in f['fliers']:
        flier.set(marker='o', color='y', alpha=0.5)

    # boxes, 箱线
    # medians, 中位值的横线,
    # whiskers, 从box到error bar之间的竖线.
    # fliers, 异常值
    # caps, error bar横线
    # means, 均值的横线
