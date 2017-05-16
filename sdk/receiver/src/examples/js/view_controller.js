/*
 *
 * Orange Default Receiver
 *
 * File name:   receiver.js
 * Version:     2.0.0
 *
 * Copyright (C) 2015 Orange
 *
 * This software is confidential and proprietary information of Orange.
 * You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the agreement you entered into.
 * Unauthorized copying of this file, via any medium is strictly prohibited.
 *
 * If you are Orange employee you shall use this software in accordance with
 * the Orange Source Charter ( http://opensource.itn.ftgroup/index.php/Orange_Source ).
 *
 */

var currentContainer, osdTimeout,
    customMessage,
    volumeTimeout, isBuffering = false, isPlaying = true, isLive = false;
const TAG = '[JoyCast]';

function toTimer(seconds) {
    var h, m, s;

    h = Math.floor(seconds / 3600);
    h = isNaN(h) ? '--' : (h >= 10) ? h : '0' + h;
    m = Math.floor(seconds / 60 % 60);
    m = isNaN(m) ? '--' : (m >= 10) ? m : '0' + m;
    s = Math.floor(seconds % 60);
    s = isNaN(s) ? '--' : (s >= 10) ? s : '0' + s;

    return h + ':' + m + ':' + s;
}
function displayHome() {
    console.log(TAG + "displayHome");
    currentContainer.hide();
    currentContainer = $('#ready-container');
    currentContainer.show();
    console.log(TAG + 'displayHome : done.');
}

function displayPlayer(bool) {
    console.log(TAG + 'displayPlayer', bool);
    currentContainer.hide();
    currentContainer = $('#player-container');
    currentContainer.show();
    console.log(TAG + 'displayPlayer : done.');
}

function movePlayer(x, y, w, h) {
    $('#media').css({
        left: x + "px",
        top: y + "px",
        width: w + "px",
        height: h + "px"
    }).show();
}

function fullscreenPlayer() {
    $('#media').css({
        left: 0,
        top: 0,
        width: 100 + "%",
        height: 100 + "%"
    }).show();
}

function displayImage(data) {
    console.log(TAG + "displayImage", data);
    currentContainer.hide();
    currentContainer = $('#image-container');
    currentContainer.show();
    console.log(TAG + 'displayImage : done.');
}

function displayError(title, message) {
    console.log(TAG + "displayError", title, message);
    currentContainer.hide();
    currentContainer = $('#error-container');
    currentContainer.show();
    $('#message-title').text(title);
    $('#message-description').text(message);
}

function displayPause(bool) {
    console.log(TAG + "displayPause ", $('state'));
    if (bool) {
        $('#state').fadeIn("fast");
    } else {
        $('#state').fadeOut("fast");
    }
}

function displayOsd(autohide) {
    console.log(TAG + "displayOsd ", autohide);
    $('#osd').fadeIn("fast");
    clearTimeout(osdTimeout);
    if (autohide) {
        osdTimeout = setTimeout(function () {
            $('#osd').fadeOut("fast");
        }, 5000);
    }
}

function displayCustomMessage(name, message) {
    console.log(TAG + "displayCustomMessage ");
    $('#custom_message').fadeIn("slow");
    $('#custom_message').text(name + ", " + message);
    clearTimeout(customMessage);
    customMessage = setTimeout(function () {
        $('#custom_message').fadeOut("slow");
    }, 6000);
}

function displayVolume(autohide) {
    console.log(TAG + "displayVolume", displayVolume);
    $('#volume').fadeIn("fast");
    clearTimeout(volumeTimeout);
    if (autohide) {
        volumeTimeout = setTimeout(function () {
            $('#volume').fadeOut("fast");
        }, 2000);
    }
}

function displayBuffering(bool) {
    console.log(TAG + "displayBuffering", bool);
    if (bool) {
        $('#buffering').fadeIn("fast");
    } else {
        $('#buffering').fadeOut("fast");
    }
}


function clearPlayer() {
    console.log(TAG + "clearPlayer");
    isBuffering = false;
    //clear media
    var mediaElement = document.getElementById('media');
    mediaElement.src = '';
    //reset osd
    $('#time-current').text('--:--:--');
    $('#time-total').text('--:--:--');
    $('#progress-bar').css('width', 0 + '%');
}
