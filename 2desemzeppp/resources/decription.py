from Crypto.PublicKey import RSA
import sys
from base64 import b64decode

cmd = open("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\encrypted", "r").read()


key=open("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\private", "r").read()
priv_key = RSA.importKey(key)
plaintext=priv_key.decrypt(b64decode(cmd))

with open("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\result", "w") as file:
	file.write(plaintext)