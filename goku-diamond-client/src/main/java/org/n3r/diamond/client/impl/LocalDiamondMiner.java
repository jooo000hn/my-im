package org.n3r.diamond.client.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.n3r.diamond.client.DiamondAxis;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static java.io.File.separator;
import static org.n3r.diamond.client.impl.Constants.*;

class LocalDiamondMiner {
    private Logger log = LoggerFactory.getLogger(LocalDiamondMiner.class);

    private Map<String/* filePath */, Long/* timestamp */> existFilesTimestamp = new HashMap<String, Long>();

    private volatile boolean running;
    private String rootPath = null;
    private FileAlterationMonitor monitor;

    public String checkLocal(DiamondMeta diamondMeta) {
        String filePath = getFilePath(diamondMeta.getDiamondAxis());
        if (!existFilesTimestamp.containsKey(filePath)) {
            if (diamondMeta.isUseLocal()) diamondMeta.clear();

            return null;
        }

        diamondMeta.setUseLocal(true);

        if (!filePath.equals(diamondMeta.getLocalFile())
                || existFilesTimestamp.get(filePath) != diamondMeta.getLocalVersion()) {
            diamondMeta.setLocalFile(filePath);
            diamondMeta.setLocalVersion(existFilesTimestamp.get(filePath));
            log.info("local changed, {}", diamondMeta.getDiamondAxis());

            return readFileContent(filePath);
        }

        return null;
    }

    public String readLocal(DiamondMeta diamondMeta) {
        String filePath = getFilePath(diamondMeta.getDiamondAxis());
        if (!existFilesTimestamp.containsKey(filePath)) {
            if (diamondMeta.isUseLocal()) diamondMeta.clear();

            return null;
        }

        // log.debug("read local, {}", diamondMeta.getDiamondAxis());

        return readFileContent(filePath);
    }

    String readFileContent(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) return null;

        try {
            return FileUtils.readFileToString(file, ENCODING);
        } catch (IOException e) {
            log.error("read file content fail {}", e.getMessage());
            return null;
        }
    }


    String getFilePath(DiamondAxis diamondAxis) {
        File file = new File(rootPath + separator
                + diamondAxis.getGroup() + separator
                + diamondAxis.getDataId() + DIAMOND_STONE_EXT);
        return file.getAbsolutePath();
    }


    public synchronized void start(DiamondManagerConf managerConfig) {
        if (running) return;

        running = true;
        rootPath = managerConfig.getFilePath() + separator + DATA_DIR;

        initDataDir();
        startCheckLocalDir();
    }

    private void watchRoot() {
        FileAlterationObserver observer = new FileAlterationObserver(rootPath);
        observer.addListener(new FileAlterationListenerAdaptor() {
            @Override
            public void onFileCreate(File file) {
                String realPath = file.getAbsolutePath();
                String grandpaDir = getGrandpaDir(realPath);
                if (!rootPath.equals(grandpaDir)
                        || !DIAMOND_STONE_EXT.equals("." + FilenameUtils.getExtension(realPath))) {
                    // log.debug("invalid file monitored {} ", file);
                    return;
                }

                existFilesTimestamp.put(realPath, System.currentTimeMillis());
                log.debug("File {} Created", realPath);
            }

            @Override
            public void onFileDelete(File file) {
                String realPath = file.getAbsolutePath();
                String grandpaDir = getGrandpaDir(realPath);
                if (rootPath.equals(grandpaDir)
                        && DIAMOND_STONE_EXT.equals("." + FilenameUtils.getExtension(realPath))) {
                    existFilesTimestamp.remove(realPath);
                    log.debug("File {} Delete", realPath);
                }
            }

            @Override
            public void onFileChange(File file) {
                String realPath = file.getAbsolutePath();
                String grandpaDir = getGrandpaDir(realPath);
                if (!rootPath.equals(grandpaDir)
                        || !DIAMOND_STONE_EXT.equals("." + FilenameUtils.getExtension(realPath))) {
                    return;
                }

                existFilesTimestamp.put(realPath, System.currentTimeMillis());
                log.debug("File {} Created", realPath);
            }

        });
        monitor = new FileAlterationMonitor(5000);
        monitor.addObserver(observer);
        try {
            monitor.start();
        } catch (Exception e) {
            log.error("start monitor fail", e);
        }
    }


    private void initDataDir() {
        try {
            new File(rootPath).mkdir();
        } catch (Exception e) {
        }
    }


    public synchronized void stop() {
        if (!running) return;
        running = false;

        if (monitor != null) try {
            monitor.stop();
        } catch (Exception e) {
            log.error("stop monitor fail", e);
        }
    }


    private void index(File file) {
        if (!file.isDirectory()) return;

        File[] subFiles = file.listFiles();
        for (File subFile : subFiles) {
            if (subFile.isDirectory()) {
                index(subFile);
            } else {
                String realPath = subFile.getAbsolutePath();
                String grandpaDir = getGrandpaDir(realPath);
                if (!rootPath.equals(grandpaDir)
                        || !DIAMOND_STONE_EXT.equals("." + FilenameUtils.getExtension(realPath))) {
                    // log.debug("invalid file monitored {} ", subFile);
                    continue;
                }

                existFilesTimestamp.put(realPath, System.currentTimeMillis());
                log.debug("{} file was added", realPath);
            }
        }
    }

    private void startCheckLocalDir() {
        index(new File(rootPath));
        watchRoot();
    }


    public String getGrandpaDir(String path) {
        File file = new File(path);
        if (file.isDirectory()) {
            log.error("{} is not a directory", path);
            return null;
        }

        File parent = file.getParentFile();
        if (parent != null) {
            File grandpa = parent.getParentFile();
            if (grandpa != null) {
                return grandpa.getAbsolutePath();
            }
        }
        log.error("fail to get grandpa of {}", path);
        return null;
    }
}
