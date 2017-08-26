

import time
from subprocess import check_output

import nxt.locator
from nxt.motor import *
from nxt.sensor import *

def spin_around(b):

	llight = Light(b, PORT_4)
	llight.set_illuminated(True)

	rlight = Light(b, PORT_3)
	rlight.set_illuminated(True)

	mode="a"

	m_left = Motor(b, PORT_A)
	m_right = Motor(b, PORT_B)
	while True:
		while mode=="a":
			ll = llight.get_lightness()
			rl = rlight.get_lightness()
			pos = ll-rl
			print ll,rl, pos

			tspd = -30
			rspd = 0

			if (pos>130):
				rspd=-40
			elif (pos>60):
				rspd = -20
			if (pos<-130):
				rspd = 40
			elif (pos<-60):
				rspd = 20

			right = (tspd+rspd)/2.0
			left = tspd-right
			
			m_right.run(right)
			m_left.run(left)

		while mode=="m":
			a = check_output(["jstest-cropped", "--normal", "/dev/input/js0"])
			x, y, z = a.split()
			tspd = 0
			rspd = 0
			x = int(x)*-1
			y = int(y)
			if (x>2000 or x<-2000):
				tspd = x/50

			if (y>2000 or y<-2000):
				rspd = y/200
		
	#		print tspd, rspd
			right = (tspd+rspd)/2.0
			left = tspd-right
		
	#		print right, left, llight.get_sample()
			print(llight.get_lightness())

			if left>100:
				left = 100
			if left<-100:
				left = -100
			if right>100:
				right = 100
			if right<-100:
				right = -100

			if left==0:
				left = 1
			if right==0:
				right = 1

			m_right.run(right)
			m_left.run(left)
	#		m_left = Motor(b, PORT_B)
	#		m_left.turn(100, 360)
	#		print ultrasonic.get_sample()
	while True:
		m_left = Motor(b, PORT_B)
		m_left.turn(100, 20)
		m_right = Motor(b, PORT_C)
		m_right.turn(-100, 20)

b = nxt.locator.find_one_brick(name="NXT", debug=True)
print("connected")
spin_around(b)

