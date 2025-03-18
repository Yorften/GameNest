import React, { Dispatch, MouseEventHandler, ReactNode, SetStateAction } from "react";

export interface RootLayoutProps {
  children: React.ReactNode;
  params: { locale: string };
}

export interface ButtonProps {
  title: ReactNode;
  className?: string;
  type?: "submit" | "button" | "reset";
  onClick?: MouseEventHandler<HTMLButtonElement>;
  id?: string;
  disabled?: boolean
}

export interface SearchBarProps {
  className?: string;
  setSelectedProducts?: React.Dispatch<React.SetStateAction<any>>;
  isNavbar?: boolean
}

export interface LocaleSwitcherProps {
  children: ReactNode;
  defaultValue: string;
  label: string;
}

export interface NavItemsProps {
  children: ReactNode;
  href: string;
  className?: string;
  target?: string;
  onClick?: MouseEventHandler<HTMLAnchorElement>;
}

export interface SubMenuProps {
  children: ReactNode;
  className?: string;
}

export interface SidebarItemProps {
  children: ReactNode;
  className?: string;
}

export interface CardProps {
  className?: string,
  children: ReactNode;
  icon?: ReactNode;
}

export interface ValuesCardProps {
  className?: string,
  item: string;
  icon?: ReactNode;
}

export interface AccordionProps {
  className?: string;
  children: ReactNode;
}

export interface AccordionItemProps {
  className?: string;
  children?: React.ReactNode;
}

export interface AccordionTitleProps {
  className?: string;
  children?: React.ReactNode;
}

export interface AccordionContentProps {
  className?: string;
  children?: React.ReactNode;
}

export interface ContactInfoCardProps {
  className?: string;
}

export interface AppContextProps {
  selectedService: any;
  selectedTerm: any;
  setSelectedService: Dispatch<SetStateAction<any>>;
  setSelectedTerm: Dispatch<SetStateAction<any>>;
}

export interface AppProviderProps {
  children: ReactNode
}

export interface ProcessCardProps {
  children: ReactNode;
  className?: string;
  order?: number;
}

export interface SideMenuButtonContentProps {
  reversed?: boolean;
  title: string;
}

export type Product = {
  id?: bigint;
  name?: string;
  brand?: string;
  description?: string;
  pdf?: string;
  category?: string;
  sub_category?: string;
  comment?: string;
  created_at?: Date;
  updated_at?: Date;
};

export interface ProductProps {
  data: Product[];
}

export type PageProps = {
  params: { [key: string]: string | string[] | undefined };
  searchParams?: { [key: string]: string | string[] | undefined };
};

export interface PaginationProps {
  page?: string;
  totalPages: number;
  hasNextPage: boolean;
};

export interface Brand {
  brand: string;
}

export interface ProductSideMenuProps {
  categories: Record<string, string[]>;
  brands: Brand[];
}

export interface ButtonAccordionProps {
  disabled?: boolean;
  className?: string;
  onClick?: MouseEventHandler<HTMLButtonElement>;
  children: ReactNode;
  title: string;
}

export interface ErrorProps {
  error: string[];
  className?: string;
}

export type selectedProducts = {
  [x: string]: {
    name: string;
    quantity: number;
    brand: string;
  };
}

export type productSensitiveData = {
  product: string,
  brand: string,
  description: string,
  quantity: number,
  price: number,
  status: string,
  isSchneider: string,
  token: string
}

export interface FormProps {
  className?: string
  submitAction(locale: string, prevState: unknown, formData: FormData, params?: any): Promise<any>
  product?: Product
  data?: productSensitiveData
}

export interface SelectedProductRowProps {
  product: {
    name: string;
    quantity: number;
    brand: string
  },
  setSelectedProducts?: React.Dispatch<React.SetStateAction<any>>;
}

export interface TeamMemberProps {
  t: any
  src: string
  member: number
  reverse?: boolean
}

export type DemandQuotation = {
  name: string
  brand: string,
  description: string,
  quantity: number,
  price: number,
  status: string,
  isSchneider: boolean
}

export interface QuotationDocumentProps {
  description?: string;
  first_name?: string;
  last_name?: string;
  company?: string;
  email?: string;
  phone_number?: string;
  status?: string;
  terms?: boolean;
  brand?: string;
  product?: string;
  quantity?: number;
  country?: string;
  quantity_input?: number;
  price?: number;
  isSchneider?: string;
  VAT?: string;
  taxes?: number;
  locale?: string;
  id?: string
}

export interface TestimonialProps {
  href: string;
  t: any;
  customer: number;
  className?: string
}