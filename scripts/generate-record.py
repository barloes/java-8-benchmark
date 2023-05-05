"""
INSERT INTO `person` (`name`, `sysDelete`, `isHuman`) VALUES
('John', 0, 1),
('Jane', 0, 1),
('Doe', 0, 0);
"""

# generate script in the follow manner. 100 000 sysDelete, 100 000 isHuman on path initdb/insert.sql

import random
import string

n = 10000
insert_string = "INSERT INTO `person` (`name`, `sysDelete`, `isHuman`) VALUES\n"
for i in range(n):
    for combinations in [(0, 0), (0, 1), (1, 0), (1, 1)]:
        random_name = ''.join(random.choice(string.ascii_lowercase) for _ in range(10))
        insert_string += "('{}', {}, {})".format(random_name, combinations[0], combinations[1])
        insert_string += ",\n" if i != n-1 or combinations != (1, 1) else ";\n"

# create file 


with open("initdb/insert.sql", "w") as f:
    f.write(insert_string)