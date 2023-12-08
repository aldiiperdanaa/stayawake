import os
import shutil
from random import sample

def create_test_dir(training_dir, test_dir, subfolders, percentage=0.2):
    if not os.path.exists(test_dir):
        os.makedirs(test_dir)

    for subfolder in subfolders:
        training_subfolder = os.path.join(training_dir, subfolder)
        test_subfolder = os.path.join(test_dir, subfolder)

        if not os.path.exists(test_subfolder):
            os.makedirs(test_subfolder)

        files = os.listdir(training_subfolder)
        selected_files = sample(files, int(len(files) * percentage))

        for file in selected_files:
            shutil.copy2(os.path.join(training_subfolder, file), test_subfolder)

subfolders_awake = ['no_yawn', 'Open', 'Open_Eyes']
subfolders_drowsy = ['Closed', 'Closed_Eyes', 'yawn']

create_test_dir('train/Awake/', 'test/Awake/', subfolders_awake)
create_test_dir('train/Drowsy/', 'test/Drowsy/', subfolders_drowsy)