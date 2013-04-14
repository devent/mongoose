#!/usr/bin/env rungroovybash
package com.anrisoftware.raids

mount = new MountCryptLvmRaid(_: this)

parameter = parse mount
parameter.valid {
    mount()
} notValid { 
    //printHelp()
    return 1
}

sda0 = DEVICES["/dev/sda0"]
sda0.isMounted()

md0 = DEVICES["/dev/md0"] // return a LVM physical volume
vg = md0.volumeGroups["vg_vostroem"] // return a LVM volume group
lv = vg.logicalVolumes["lv_data0"] // return a LVM logical volume
lv.isActive()
lv.setActive(true)
lv.isMounted()

crypt = luksOpen "/dev/vg_data_extern/lv_data0", "data0" // luksOpen build-in
crypt = luksOpen lv, "data0"
crypt.isMounted()
crypt.checkFilesystem()
crypt.setMounted("/media/data0" /*, true*/) // defaults to true
crypt.setMounted("/media/data0", false)

class MountRaid {

    def _

    @Option(name = "-raid-name", aliases = ["-name", "-n"], required = true)
    String raidName
    
    @Option(name = "-raid-device", aliases = ["-device", "-d"], required = true)
    String raidDevice
    
    def mount() {
        _.echo "mount raid"
    }
    
    def umount() {
        _.echo "umount raid"
    }
    
    boolean isMounted() {
        _.echo "is mounted"
        return true
    }
}

class MountLvmRaid extends MountRaid {

    @Option(name = "-volume-group-name", aliases = ["-group", "-g"], required = true)
    String volumeGroupName
    
    @Option(name = "-volume-group-name-prefix", aliases = ["-group-prefix"])
    String volumeGroupNamePrefix = "/dev/vg_"
    
    @Option(name = "-logical-volume-name-prefix", aliases = ["-volume-prefix"])
    String logicalVolumeNamePrefix = "lv_"
    
    def mount() {
        super.mount()
        _.run asRoot: true, """\
            lvchange -a y \
            /dev/${volumeGroupNamePrefix}\
            ${volumeGroupName}/\
            ${logicalVolumeNamePrefix}${raidName}"""
    }
    
    def umount() {
        super.umount()
        _.run asRoot: true, """\
            lvchange -a n \
            /dev/${volumeGroupNamePrefix}${volumeGroupName}/\
            ${logicalVolumeNamePrefix}${raidName}"""
    }
    
    boolean isMounted() {
        def mounted = super.isMounted()
        return mounted && checkMounted()
    }
    
    boolean checkMounted() {
        _.run asRoot: true, """\
            lvchange -a n \
            /dev/${volumeGroupNamePrefix}${volumeGroupName}/\
            ${logicalVolumeNamePrefix}${raidName}"""
    }
}

class MountCryptLvmRaid extends MountLvmRaid {

    @Option(name = "-passphrase", aliases = ["-P"])
    String passphrase
    
    def call() {
        super.call()
        _.echo "mount crypt"
    }
}

