async function afterClickByClass(disableClass, openid) {
    await disableByClass(disableClass);
    await hideByClass(disableClass)
    openById(openid);
}

async function naviClickByClass(disableClass, hideClass, openid, attrName, dataName) {
    await disableByClass(disableClass);
    await hideByClass(hideClass)
    await hideByDataName(dataName)
    openById(openid);
}

async function disableButton(button) {
    button.disabled = true;
    button.style.background = '#111111';
    button.style.opacity = '0.5';
    button.value = "Please wait...";
}

async function disableByClass(hideClass) {
    const elements = await document.getElementsByClassName(hideClass);
    Array.from(elements)?.forEach(item => {
        if (item) {
            item.style.background = '#111111';
            item.style.opacity = '0.5';
            item.disabled = true;
        }
    });
}

async function hideByClass(hideClass) {
    const elements = await document.getElementsByClassName(hideClass);
    Array.from(elements)?.forEach(item => {

        if (item) {
            item.style.display = 'none';
        }
    });
}

async function hideByDataName(dataName) {
    const elements = await document.querySelectorAll(dataName);
    Array.from(elements)?.forEach(item => {
        if (item) {
            item.style.display = 'none';
        }
    });
}

function openById(openid) {
    const element = document.getElementById(openid);
    if (element) {
        element.style.display = 'block';
    }
}

function hideById(openid) {
    const element = document.getElementById(openid)
    if (element) {
        element.style.display = 'none';
    }
}

<!-- requires button with id "gototopbttn" -->
window.onscroll = function () {
    const gototopbttn = document.getElementById("gototopbttn");
    if (gototopbttn) {
        scrollFunction(gototopbttn);
    }
};

function scrollFunction(gototopbttn) {
    if (document.body.scrollTop > 50 || document.documentElement.scrollTop > 50) {
        gototopbttn.style.display = "block";
    } else {
        gototopbttn.style.display = "none";
    }
}

function topFunction() {
    document.body.scrollTop = 0; // For Safari
    document.documentElement.scrollTop = 0; // For Chrome, Firefox, IE and Opera
}



