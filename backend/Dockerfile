FROM python:3.7-slim

WORKDIR /code

ADD code/ .

RUN pip install -r requirements.txt

ENV FLASK_APP=__init__.py

CMD ["flask", "run", "-h", "0.0.0.0", "-p", "7070"]