const borders = [];
borders["X"] = ["-5", "3"];
borders["Y"] = ["-4", "4"];
borders["R"] = ["1", "5"];

function validate(form) {

    let X = form.X.value;

    let R = [];
    for (var i = 0; i < form.R.length; i++) {
        if (form.R[i].checked) {
            R.push(form.R[i].value);
        }
    }



    let Y = form.Y.value;

    let valid = true;

    if (!(isPresented(X, "X", true) && validateTextParam(X, "X", true))) {
        valid = false;
    }
    if (!(isPresented(R, "R", true))) {
        valid = false;
    }
    if (!(isPresented(Y, "Y", true) && validateTextParam(Y, "Y", true))) {
        valid = false;
    }
    return valid;

}

function isPresented(param, paramName, warn) {
    if (param == "" || param == null || param.length == 0 || (paramName=="R" && param.length > 1)) {
        if (warn) {
            showWarning(paramName + " некорректно указан", paramName);
        }
        return false;
    } else {
        showWarning("", paramName);
    }

    return true;
}

function validateTextParam(param, paramName, warn) {
    if (!(!isNaN( Number(param) ) && param.lastIndexOf('.') != (param.length - 1))) {
        if (warn) {
            showWarning(paramName + " должен быть числом", paramName);
        }
        return false;
    }
    let bottomBorder = borders[paramName][0];
    let topBorder = borders[paramName][1];

    if (!(Number(param) > bottomBorder && Number(param) < topBorder)) {
        if (warn) {
            //showWarning(paramName + " не входит в необходимый диапазон (" + bottomBorder + " ... " + topBorder +")", paramName);
            showWarning(paramName + " некорректно указан", paramName);
        }
        return false;
    }
    showWarning("", paramName);
    return true;
}

function showWarning(warningMessage, paramName) {

    let warningContainer = document.getElementById("warning-container-" + paramName);

    if (warningContainer != null) {
        warningContainer.textContent = warningMessage;
    }

}