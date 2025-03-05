
export interface User {
id?: number;
firstName?: string;
lastName?: string;
email?: string;
password?: string;
address?: string;
phone?: string;
title?: string;
bio?: string;
imageUrl?: string;
enabled?: boolean;
nonLocked?: boolean;
usingMfa?: boolean;
createdAt?: Date;
roleName?: string[];
permission?: string;
}


