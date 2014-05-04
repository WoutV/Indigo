from Crypto.PublicKey import RSA
import base64

key= open("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\public","r").read()
pub_key = RSA.importKey(key)
cmd_enc = pub_key.encrypt("Oh yea bitzeses",0)[0]

with open("C:\\Users\\Study\\Documents\\GitHub\\Indigo\\2desemzeppp\\keys\\encrypted", "w") as file:
	file.write(cmd_enc.encode("base64"))