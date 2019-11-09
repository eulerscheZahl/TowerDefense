#! /usr/bin/env python
import re

with open('../src/main/java/TowerDefense/Constants.java') as f:
    constants = f.read()
var_values = {}
for line in constants.split('\n'):
    if not '=' in line: continue
    assign = line.split('=')
    left = assign[0].strip().split()[-1]
    right = assign[1].strip()
    right = right[0:right.index(';')]
    if '{' in right:
        right = right.replace('{', '').replace('}', '').split(',')
        for i in range(0, len(right)):
            var_values[left+'_'+str(i)] = right[i].strip()
    else:
        var_values[left] = right;

with open('statement_en.template') as f:
    statement = f.read()

for var, val in var_values.items():
    statement = statement.replace('[['+var+']]', '<const>' + val + '</const>')

with open('statement_en.html', 'w') as f:
    f.write(statement)
