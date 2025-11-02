(function () {
    function ensureCss(href) {
        if (![...document.styleSheets].some(s => s.href && s.href.includes(href))) {
            const link = document.createElement('link');
            link.rel = 'stylesheet';
            link.href = href;
            document.head.appendChild(link);
        }
    }

    function injectFooter(footerEl) {
        const target = document.getElementById('footer') || document.querySelector('footer#footer');
        if (target) {
            target.replaceWith(footerEl);
        } else {
            document.body.appendChild(footerEl);
        }
    }

    async function loadFooter() {
        try {
            const res = await fetch('footer.html', { cache: 'no-store' });
            const html = await res.text();
            const tpl = document.createElement('template');
            tpl.innerHTML = html;
            const footer = tpl.content.querySelector('#footer');
            if (!footer) return;
            // clone to avoid moving out of template
            const footerClone = footer.cloneNode(true);
            injectFooter(footerClone);

            // optionally ensure css exists
            ensureCss('assert/css/base.css');
            ensureCss('assert/css/footer.css');
        } catch (e) {
            // silently fail
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', loadFooter);
    } else {
        loadFooter();
    }
})();


