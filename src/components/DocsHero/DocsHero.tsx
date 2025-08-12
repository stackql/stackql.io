import React from "react";
import styles from "./DocsHero.module.css";
import ThemedImage from "@theme/ThemedImage";

type Props = {
  lightSrc: string;
  darkSrc: string;
  alt?: string;
  title: React.ReactNode;   // HEADLINE
  byline: React.ReactNode;  // BYLINE
};

export default function DocsHero({ lightSrc, darkSrc, alt = "", title, byline }: Props) {
  return (
    <section className={styles.wrap} aria-label="StackQL hero">
      <div className={styles.logo}>
        <ThemedImage
          alt={alt}
          sources={{ light: lightSrc, dark: darkSrc }}
        />
      </div>

      <div>
        <h2 className={styles.headline}>{title}</h2>
        <br/>
        <div className={styles.byline}>{byline}</div>
      </div>
    </section>
  );
}
