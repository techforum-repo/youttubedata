// html-include.js

class HTMLIncludeElement extends HTMLElement {
  get src() {
    return this.getAttribute('src');
  }

  set src(value) {
    this.setAttribute('src', value);
  }

  get mode() {
    return this.getAttribute('mode');
  }

  set mode(value) {
    this.setAttribute('mode', value);
  }


  connectedCallback() {

    const url = this.getAttribute('src');
    const shadowRoot = this.attachShadow({ mode: 'closed' });

    this.fetchHTMLResponse(url).then(
      ({ html, err }) => {
        if (err) {
          throw new Error(`html-include fetch failed: ${response.statusText}`);
        }

        shadowRoot.innerHTML = html;

      }
    );

  }

  async fetchHTMLResponse(url) {
    const mode = this.mode || 'cors';
    let html;
    let err;

    try {
      const response = await fetch(url, { mode });

      if (!response.ok) {
        console.log('ERROR:Could not load HTML data');
      }

      html = await response.text();


    } catch (err) {

      return { html: null, err };

    }

    return { html, err };
  }
}

customElements.define('html-include', HTMLIncludeElement);
