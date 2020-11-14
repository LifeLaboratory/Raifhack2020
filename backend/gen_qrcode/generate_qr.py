import qrcode
import datetime


def generate(data):
    img_name = "qr-{}.png".format(datetime.datetime.now()).replace(" ", "_").replace("-", "_").replace(":", "_")
    img = qrcode.make(data, border=1)
    img.save(img_name)
    return img

#generate(url)