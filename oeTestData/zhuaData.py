from selenium import webdriver
import random
import time
import string

for k in range(0, 13):
    browser = webdriver.Chrome()
    game = 12

    url = "http://localhost:8181"
    browser.get(url)
    time.sleep(0.5)
    for gameNum in range(0, game):
        buttonCreate = browser.find_element_by_id('create_room')
        buttonCreate.click()

        weightArray = browser.find_elements_by_css_selector('.multi-scenarios')

        a = random.randint(0, 4)
        time.sleep(0.2)
        weightArray[a].click()

        buttonSubbmit = browser.find_element_by_id('createChoose')
        buttonSubbmit.click()
        time.sleep(0.2)

    for i in range(0, game * 3):

        js = 'window.open("http://localhost:8181")'  # 如果是空的则打开的标签页为空
        browser.execute_script(js)
        handlers = browser.window_handles  # 获取当前页面的句柄
        htmlNum = handlers.__len__()
        browser.switch_to.window(handlers[htmlNum - 1])  # 切换第几个
        time.sleep(1)

        buttonPlayer = browser.find_element_by_id('start_player')
        buttonPlayer.click()

        # 创建玩家
        inputName = browser.find_element_by_xpath('//*[@id="input"]/p[1]/input')
        name = ''.join(random.sample(string.ascii_letters + string.digits, 5))
        inputName.send_keys(name)
        time.sleep(0.2)

        room = browser.find_elements_by_css_selector('.room_block')
        roNum = browser.find_elements_by_xpath('//span[contains(text(), "/3")]')
        for j in roNum:
            roNumint = int((j.text)[0])
            if (roNumint < 3):
                j.click()
                break

        time.sleep(0.5)

        agent = browser.find_element_by_xpath('//*[@id="agent"]')
        agent.click()
        agentChoose = browser.find_elements_by_xpath('//*[@id="choose_agent"]/option')
        # if i == 0:
        b = 0
        # else:
        #     b = random.randint(1, 5)
        agentChoose[b].click()

        buttonOK = browser.find_element_by_xpath('//*[@id="input"]/p[6]/input[2]')
        buttonOK.click()

    time.sleep(10)
    browser.quit()  # 关闭浏览器
