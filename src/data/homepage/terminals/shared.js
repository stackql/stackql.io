export const spinner = ['⠋', '⠙', '⠹', '⠸', '⠼', '⠴', '⠦', '⠧', '⠇', '⠏'];

export const spinnerRepeatCount = 3;

export function getSpinnerFrames(s) {
    return {
        text: s + ' Running query',
        delay: 10
    }
}

export const termLineDelay = 40;