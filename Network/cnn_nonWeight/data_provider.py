import xml.sax


def get_file_data(file_name):
    all_data = []

    def add_piece(piece_data):
        all_data.append(piece_data)

    class MovieHandler(xml.sax.ContentHandler):
        def __init__(self):
            self.players = {}
            self.players_number = 0
            self.piece_data = [0] * 4

        # 元素开始事件处理
        def startElement(self, tag, attributes):
            if tag == 'knowledge':
                player_name = attributes['role-name']
                self.players[player_name] = {}
                self.players[player_name]['id'] = int(attributes['party-num'])
                self.players[player_name]['resources'] = int(attributes['resource'])

            if tag == 'proposal' or tag == 'ally':
                player_name = attributes['role-name']
                value = int(attributes['reward'])
                player_id = self.players[player_name]['id']
                self.piece_data[player_id] = 1.0 * value / 100

            if tag == 'response':
                player_agree = attributes['agree']
                if (player_agree == 'false'):
                    self.piece_data[3] = 1

        # 元素结束事件处理
        def endElement(self, tag):
            if tag == 'proposal':
                self.piece_data = [0] * 4
                add_piece(self.piece_data)

        # 内容事件处理
        def characters(self, content):
            pass

    parser = xml.sax.make_parser()
    parser.setFeature(xml.sax.handler.feature_namespaces, 0)
    Handler = MovieHandler()
    parser.setContentHandler(Handler)
    parser.parse(file_name)

    return all_data.copy()


'''
if ( __name__ == "__main__"):
   
   # 创建一个 XMLReader
   parser = xml.sax.make_parser()
   # turn off namepsaces
   parser.setFeature(xml.sax.handler.feature_namespaces, 0)
 
   # 重写 ContextHandler
   Handler = MovieHandler()
   parser.setContentHandler(Handler)

   parser.parse("./data/150835480_Scene0.xml")
   print(all_data)

'''
