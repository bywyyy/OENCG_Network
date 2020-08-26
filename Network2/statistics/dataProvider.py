import xml.sax

agentArray = ['AgentTS', 'AgentTS2', 'AgentNetwork', 'AgentTraverse', 'Agent', 'Human', 'AgentTraverse2',
              'ReinforceAgent', 'AgentProselfA', 'AgentProselfB']


def get_file_data(file_name):
    all_data = []
    def add_piece(piece_data):
        all_data.append(piece_data)
    class MovieHandler(xml.sax.ContentHandler):
        def __init__(self):
            self.players = {}
            self.players_number = 0
            self.piece_data = [0] * 12
            self.rolenameAgent = {}
            self.round = 0
            '''数据为12位
            [0]:提议者的agent在agentArray数组中的index
            [1]:proposal的label,0为接收，1为拒绝
            [2]:当前round
            [3-5]:payoff,
            [6-8]:player权重,权重仅在读取每个文件的第一个数组包含
            [9-11]:player获得offer的顺序，每个文件的第一个数组包含
            '''
        # 元素开始事件处理
        def startElement(self, tag, attributes):
            if tag == 'game':
                self.piece_data[9] = int(attributes['majority'])  # 读取game的majority信息
            if tag == 'session':
                self.round = 0
            if tag == 'knowledge':
                player_name = attributes['role-name']
                self.players[player_name] = {}
                self.players[player_name]['id'] = int(attributes['party-num'])
                self.players[player_name]['resources'] = int(attributes['resource'])
                weight = int(attributes['resource'])
                player_id = self.players[player_name]['id']
                # self.piece_data[player_id + 9] = 1.0 * weight  # 读取权重信息
                self.piece_data[player_id + 6] = 1.0 * weight  # 读取权重信息
            if tag == 'player':
                player_name = attributes['role-name']
                player_agent = attributes['path']
                self.rolenameAgent[player_name] = player_agent
                player_id = self.players[player_name]['id']
                self.piece_data[player_id + 9] = agentArray.index(self.rolenameAgent[player_name])
            if tag == 'round':
                roundNum = int(attributes['num'])
                round = roundNum % 15
                if (round == 0):
                    self.piece_data[2] = 15
                else:
                    self.round += 1
                self.piece_data[2] = self.round  # 读取当前round
            if tag == 'proposal':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[0] = agentArray.index(self.rolenameAgent[player_name])  # 写入agent在对应数组中的index
                self.piece_data[player_id + 3] = 1.0 * value
                # self.piece_data[player_id + 6] = 1  # 提议者视为接收联盟，state置为1
            if tag == 'ally':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id + 3] = 1.0 * value
            if tag == 'response':
                player_agree = attributes['agree']
                if (player_agree == 'false'):
                    self.piece_data[1] = 1  # proposal的label
        # 元素结束事件处理
        def endElement(self, tag):
            if tag == 'round':
                add_piece(self.piece_data)
                self.piece_data = [0] * 12
        # 内容事件处理
        def characters(self, content):
            pass
    parser = xml.sax.make_parser()
    parser.setFeature(xml.sax.handler.feature_namespaces, 0)
    Handler = MovieHandler()
    parser.setContentHandler(Handler)
    parser.parse(file_name)
    return all_data.copy()