import threading
import os

def task1():
    os.system("java Server")
t1=threading.Thread(target=task1, name='t1')
t1.start()
while True:
        if t1.is_alive() is False:
                t1.join()
                t1=threading.Thread(target=task1, name='t1')
                t1.start()