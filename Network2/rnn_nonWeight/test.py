from  agentDataSet import agentData

if __name__ == '__main__':
    learn_data = agentData('data', 3)
    print(learn_data.__len__)
    print(learn_data[1])
